package com.wcwy.common.redis.util;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.wcwy.common.base.utils.DateUtils;
import com.wcwy.common.base.utils.RandomUtils;
import com.wcwy.common.base.utils.RedisKeyContants;
import com.wcwy.common.base.utils.SendDingDing;
import com.wcwy.common.redis.entity.MQEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author：
 */
@Component
public class RedisUtils {

    /**
     * 注入redisTemplate bean
     */
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((List<String>) CollectionUtils.arrayToList(key));
            }
        }
    }
    // ============================String(字符串)=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }
    public long incr1(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }
    public long incrTime3(String key, long delta) {
        Long increment = redisTemplate.opsForValue().increment(key, delta);
        LocalDateTime midnight = LocalDateTime.now().plusDays(3).withHour(0).withMinute(0).withSecond(0).withNano(0);
        long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), midnight);
        expire(key, seconds);
        return increment;
    }
    public long incrTime(String key) {

        ValueOperations<String, Object> stringObjectValueOperations = redisTemplate.opsForValue();
        Long increment = stringObjectValueOperations.increment(key);
        if (increment == 1) {
            LocalDateTime midnight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), midnight);
            expire(key, seconds);
        }
        return increment;
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }
    // ================================Hash(哈希)=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }
    // ============================Set(集合)=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    // ===============================List(列表)=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Long leftPush(String key, String s) {
        try {
            Long aLong = redisTemplate.opsForList().leftPush(key, s);
            return aLong;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSets(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据正则表达式获取key列表
     *
     * @param patternKey 正则表达式
     * @return 匹配key列表
     */
    public Set<String> keys(String patternKey) {
        try {
            Set<String> keys = redisTemplate.keys(patternKey);
            return keys;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    /**
     * mq将发送未成功数据放入set缓存
     *
     * @param key 键
     * @param msg 值
     * @return 成功个数
     */
    public long sendSet(String key, String msg, Throwable e1) {

        try {

            SendDingDing.sendText("MQ：错误信息："+e1.toString()+",TOPIC："+key+"msg"+msg);
            MQEntity mqEntity = new MQEntity();
            mqEntity.setDate(msg);
            if (e1 != null) {
                mqEntity.setException(e1);
            }
            return redisTemplate.opsForSet().add("MQ" + key, JSONUtil.toJsonStr(mqEntity));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Set<Object> getSet(String key) {
        try {
            Set<Object> members = redisTemplate.opsForSet().members("MQ" + key);
            return members;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除数据缓存的值
     *
     * @param key
     * @param date
     * @return
     */
    public Long delSet(String key, Object date) {
        try {
            Long remove = redisTemplate.opsForSet().remove("MQ" + key, date);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * mq将未消费成功数据放入set缓存
     *
     * @param key 键
     * @param msg 值
     * @return 成功个数
     */
    public long acceptSet(String key, String msg) {
        try {
            SetOperations<String, Object> stringObjectSetOperations = redisTemplate.opsForSet();
            Boolean member = stringObjectSetOperations.isMember(key, msg);
            if (!member) {
                return redisTemplate.opsForSet().add("MQ" + key, msg);
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public Boolean setIfAbsentMinutes(String key, String msg, long time) {
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, msg, time, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    /**
     * mq消费一次验证
     *
     * @param key
     * @param msg
     * @param time
     * @return
     */

    public Boolean setIfAbsent(String key, String msg, long time) {
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, msg, time, TimeUnit.HOURS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public Boolean setIfAbsent(String key, String msg) {
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * @return null
     * @Description: 生成唯一标识
     * @Author tangzhuo
     * @CreateTime 2023/3/30 14:18
     */

    public String generateCode() {
        String prefix = "mq";
        String currentDt = DateUtils.getCurrentDateStr(DateUtils.DATE_PARENT_YYMMDDHHMMSS_SSS);
        Long incrtNum = generate(prefix, DateUtils.getTodayEndTime());
        if (null != incrtNum) {
            return prefix + currentDt + "-" + incrtNum;
        }
        String random = RandomUtils.generateRandomNum(6);
        return prefix + currentDt + "-" + random;
    }


    public Long generate(String key, Date expireTime) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        counter.expireAt(expireTime);
        return counter.incrementAndGet();
    }

    public void msgAddList(String userId, Object msg) {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        listOperations.rightPush(userId, msg);
    }

    public List<Object> msgGetList(String userId) {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        Long total = listOperations.size(userId);
        List<Object> students = listOperations.range(userId, 0, total);
        redisTemplate.delete(userId);
        return students;
    }


    /**
     * 存储验证码
     *
     * @param identification:标识
     * @param phone:电话号码
     * @param code:验证码
     * @return
     */
    public Map<String, Object> verificationCode(String identification, String phone, String code, long time) {
        Map map = new ConcurrentHashMap(2);
        //long milliSecondsLeftToday = 86400000 - org.apache.commons.lang3.time.DateUtils.getFragmentInMilliseconds(Calendar.getInstance(), Calendar.DATE);
        long secondsLeftToday = 86400 - org.apache.commons.lang3.time.DateUtils.getFragmentInSeconds(Calendar.getInstance(), Calendar.DATE);
        long incr = incr(phone, 1);
        expire(phone, secondsLeftToday);
        if (incr > 3) {
            Object o = get(identification + phone);
            if (!StringUtils.isEmpty(o)) {
                map.put("isOk", false);
                map.put("msg", "您有验证码未使用,请不要重复发送！");
                return map;
            }
            boolean set = set(identification + phone, code, time);
            map.put("isOk", set);
            return map;
        }
        boolean set = set(identification + phone, code, time);
        map.put("isOk", set);
        return map;

    }

    public Set<Object> SortedSet(String key,String chatId,int i) {
        ValueOperations ops = redisTemplate.opsForValue();
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        Long age = ops.increment("newest",i);
        Double c= Double.valueOf(age);
        ZSetOperations.TypedTuple<Object> objectTypedTuple =
                new DefaultTypedTuple<Object>(chatId, c);
        Set<ZSetOperations.TypedTuple<Object>> tuples = new
                HashSet<ZSetOperations.TypedTuple<Object>>();
        tuples.add(objectTypedTuple);
        zSetOperations.add(key,tuples);
        return zSetOperations.range(key, 0, -1);
    }


    public Set<Object> gerSortedSet(String key) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        Set<Object> range = zSetOperations.range(key, 0, -1);
        return range;
    }
}
