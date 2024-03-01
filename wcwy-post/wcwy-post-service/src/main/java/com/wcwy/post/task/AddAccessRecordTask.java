package com.wcwy.post.task;

import com.alibaba.fastjson.JSON;
import com.wcwy.common.base.utils.DateUtils;
import com.wcwy.common.base.utils.StringUtils;
import com.wcwy.common.base.vo.IpLocation;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.post.vo.AccessRecordVO;
import com.wcwy.system.entity.AccessRecord;
import com.wcwy.system.service.AccessRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static com.wcwy.common.base.utils.IpUtils.getLocation;

/**
 * ClassName: AddAccessRecordTask
 * Description:
 * date: 2023/8/1 13:59
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Configuration
@Slf4j
public class AddAccessRecordTask {
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private AccessRecordService accessRecordService;

   // @Scheduled(cron ="0 0 0/1 * * ?")
    @Scheduled(cron = "0/1200 * * * * ?")
  // @Scheduled(cron = "0/3 * * * * ?")
    public void calculateAddAccessRecord() throws IOException {
        HashSet<String> hashSet = new HashSet();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -1);
        Date time = cal.getTime();
        String dateStr = DateUtils.getDateStr(time, DateUtils.DATE_PATTERN_YYMMDDHH);
        long l = redisUtils.lGetListSize("AR" + dateStr);
        List<Object> ar = redisUtils.lGet("AR" + dateStr, 0, l);
        if(ar==null && ar.size()<0){
            return;
        }
        for (Object s : ar) {
            AccessRecordVO accessRecordVO = JSON.parseObject(s.toString(), AccessRecordVO.class);
            if (StringUtils.isEmpty(accessRecordVO.getProvince())) {
                continue;
            }
            try {
                redisUtils.lRemove("AR" + dateStr,1,s);
            }catch (Exception e){
                log.error("删除报错了"+e);
            }
            hashSet.add(accessRecordVO.getIpAddress() + accessRecordVO.getType());
            redisUtils.sSet(accessRecordVO.getIpAddress() + accessRecordVO.getType(), accessRecordVO.getIpAddress());
            redisUtils.incr("duration" + accessRecordVO.getIpAddress() + accessRecordVO.getType(), accessRecordVO.getSecond());
            redisUtils.incr("visit" + accessRecordVO.getIpAddress() + accessRecordVO.getType(), 1);

        }

        long rs = redisUtils.lGetListSize("RS" + dateStr);
        List<Object> rsList = redisUtils.lGet("RS" + dateStr, 0, rs);
        for (Object o : rsList) {
            AccessRecordVO accessRecordVO = JSON.parseObject(o.toString(), AccessRecordVO.class);
            if (StringUtils.isEmpty(accessRecordVO.getProvince())) {
                continue;
            }
            try {
                redisUtils.lRemove("RS" + dateStr,1,o);
            }catch (Exception e){
                log.error("删除报错了"+e);
            }
            hashSet.add(accessRecordVO.getIpAddress() + accessRecordVO.getType());
            redisUtils.incr("register" + accessRecordVO.getIpAddress() + accessRecordVO.getType(), accessRecordVO.getRegister());

        }


        Iterator<String> iterator = hashSet.stream().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (next.length() < 2) {
                continue;
            }

            String substring = next.substring(0, next.length() - 1);
            IpLocation location = getLocation(substring);

            AccessRecord accessRecord = new AccessRecord();
            accessRecord.setProvince(location.getProvince());
            accessRecord.setCity(location.getCity());
            accessRecord.setType(next.substring( next.length() - 1,next.length()));
            accessRecord.setCreateTime(LocalDateTime.now());
            Object o1 = redisUtils.get("visit" + next);
            Object o = redisUtils.get("duration" + next);
            if(o1 !=null && o !=null){
                Long visit = Long.valueOf(String.valueOf(o1));
                Long duration =Long.valueOf(String.valueOf(o));
                System.out.println(visit);
                accessRecord.setVisit(visit);
                double i = duration / visit;
                accessRecord.setSecond((long) i);

            }


            Integer register = (Integer) redisUtils.get("register" + next);
            if(register !=null){
                accessRecord.setRegister((long)register);
            }
            accessRecord.setIpAddress(redisUtils.sGetSetSize(next));

            redisUtils.del("visit" + next);
            redisUtils.del("duration" + next);
            redisUtils.del("register" + next);
            redisUtils.del(next);
            hashSet=null;

          try {
              boolean save = accessRecordService.save(accessRecord);
          }catch (Exception e){
              log.error("报错了");
              log.error(accessRecord.toString());
          }
        }

        redisUtils.del("AR" + dateStr);
        redisUtils.del("RS" + dateStr);
    }
}
