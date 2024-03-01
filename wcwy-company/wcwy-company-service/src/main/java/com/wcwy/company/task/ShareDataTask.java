package com.wcwy.company.task;

import com.alibaba.fastjson.JSON;
import com.wcwy.common.base.enums.ShareDataEnums;
import com.wcwy.common.base.utils.DateUtils;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.entity.ShareData;
import com.wcwy.company.po.ShareDataPO;
import com.wcwy.company.service.ShareDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: ShareDataTask
 * Description:
 * date: 2023/8/25 17:24
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Slf4j
@Component
public class ShareDataTask {
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private ShareDataService shareDataService;

    // @Scheduled(cron ="0 0 0/1 * * ?")
   // @Scheduled(cron = "0 0/30 * * * ?")
    @Scheduled(cron = "0/3 * * * * ?")
    public void calculateAddAccessRecord() {
        log.info("执行访问记录。。。。。。。。。。。。。。。。。");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -0);
        Date time = cal.getTime();
        String dateStr = DateUtils.getDateStr(time, DateUtils.DATE_PATTERN_YYMMDDHH);
        //存储推荐官信息
        Set<Object> objects = redisUtils.sGet(ShareDataEnums.SD_USER_ID.getShareData()+dateStr);
        Iterator<Object> iterator = objects.iterator();
        // 遍历Set集合中的元素
        while (iterator.hasNext()) {
            String userid = (String) iterator.next();

            //获取到总分享次数
            long l = redisUtils.lGetListSize(ShareDataEnums.SD_POST_SHARING.getShareData() + userid + dateStr);
            int i = (int) (l / 3);
            Map<String, Integer> map = new ConcurrentHashMap(i);//存储每个地址分享的次数
            List<Object> objects1 = redisUtils.lGet(ShareDataEnums.SD_POST_SHARING.getShareData() + userid + dateStr, 0, l);
            //区分里面的省市
            for (Object o : objects1) {
                Integer integer = map.get(o);
                if (integer == null) {
                    integer = 0;
                }
                integer = integer + 1;
                map.put(o.toString(), integer);
            }
            long inviteLinkSize = redisUtils.lGetListSize(ShareDataEnums.INVITE_LINK.getShareData() + userid + dateStr);
            int inviteLinkLength = (int) (l / 3);
            //区分访问类型
            Map<Integer, Integer> inviteLinkMap = new ConcurrentHashMap(inviteLinkLength);//存储每个地址分享的次数
            List<Object> inviteLinkMapObjects = redisUtils.lGet(ShareDataEnums.INVITE_LINK.getShareData() + userid + dateStr, 0, inviteLinkSize);
            if(inviteLinkMapObjects.size()>0){
                for (Object inviteLinkMapObject : inviteLinkMapObjects) {
                    ShareDataPO shareDataPO = JSON.parseObject(inviteLinkMapObject.toString(), ShareDataPO.class);
                    Integer type = shareDataPO.getType();
                    if(inviteLinkMap.get(type)==null){
                        inviteLinkMap.put(type,0);
                    }
                    inviteLinkMap.put(type,type+1);
                }

            }



            //获取简历被下载次数
            long downloadSize = redisUtils.lGetListSize(ShareDataEnums.DOWNLOAD_POST.getShareData() + userid + dateStr);
            int downloadLength = (int) (downloadSize / 3);
            Map<String, Integer> downloadMap = new ConcurrentHashMap(downloadLength);//存储每个地址分享的次数
            List<Object> downloads = redisUtils.lGet(ShareDataEnums.DOWNLOAD_POST.getShareData() + userid + dateStr, 0, downloadSize);
            //区分里面的省市
            for (Object o : downloads) {
                Integer integer = downloadMap.get(o);
                if (integer == null) {
                    integer = 0;
                }
                integer = integer + 1;
                downloadMap.put(o.toString(), integer);
            }


            //注册数统计
            long insertJobHunter = redisUtils.lGetListSize(ShareDataEnums.INSERT_USER.getShareData() + userid + dateStr);
            int insertJobHunterCount = (int) (l / 3);
            Map<String, Integer> insertJobHunterMap = new ConcurrentHashMap(insertJobHunterCount);//存储求职者注册数
            List<Object> insertJobHunterObjects = redisUtils.lGet(ShareDataEnums.INSERT_USER.getShareData() + userid + dateStr, 0, insertJobHunter);
            if (insertJobHunterObjects != null && insertJobHunterObjects.size() > 0) {
                insertJobHunterMap = statistics(insertJobHunterObjects);
            }





            List<Object> objects2 = redisUtils.lGet(ShareDataEnums.SD_POST_SHARING_RECOMMEND.getShareData() + userid + dateStr, 0, redisUtils.lGetListSize(ShareDataEnums.SD_POST_SHARING_RECOMMEND.getShareData() + userid + dateStr));
            Map<String, Long> accessTime = new HashMap<>(objects2.size() / 2);//访问总时长
            Map<String, HashSet<String>> ip = new HashMap<>(objects2.size() / 2);//ip数量
            Map<String, Integer> visitTimes = new HashMap<>(objects2.size() / 2);//访问次数
            for (Object o : objects2) {
                ShareDataPO shareDataPO = JSON.parseObject(o.toString(), ShareDataPO.class);
                //根据省市来区分
                Map map1 = new HashMap(3);
                map1.put("province", shareDataPO.getProvince());
                map1.put("city", shareDataPO.getCity());
                map1.put("type", shareDataPO.getType());
                String s = JSON.toJSONString(map1);
                //访问次数
                Integer aLong1 = visitTimes.get(s);
                if (aLong1 == null) {
                    aLong1 = 0;
                }
                aLong1= aLong1+1;
                visitTimes.put(s,aLong1);
                //访问时长
                Long aLong = accessTime.get(s);
                if (aLong == null) {
                    aLong = 0L;
                }
                aLong = shareDataPO.getSecond() + aLong;
                accessTime.put(s, aLong);//重新缓存
                HashSet<String> strings = ip.get(s);
                if (strings == null) {
                    strings = new HashSet<>();
                }
                strings.add(shareDataPO.getIpAddress());
                ip.put(s, strings);
            }
            Set<String> strings = ip.keySet();
            Iterator<String> iterator1 = strings.iterator();
            List<ShareData> list = new ArrayList(ip.size());
            while (iterator1.hasNext()) {
                String userid1 = iterator1.next();
                //获取ip总数
                HashSet<String> strings1 = ip.get(userid1);

                ShareData shareData = new ShareData();
                Map map1 = JSON.parseObject(userid1, Map.class);
                //获取注册量
                Integer integer1 = insertJobHunterMap.get(userid1);
                if (integer1 != null) {
                    shareData.setRegister((long) integer1);
                }

                shareData.setCity(map1.get("city").toString());
                shareData.setProvince(map1.get("province").toString());
                shareData.setIpAddress((long) strings1.size());
                String type1 = map1.get("type").toString();
                // System.out.println(type1);
                Integer type = Integer.parseInt(type1);
                //访问类型
                shareData.setType(type);
                //如果等于职位分享  获取分享次数
                if (type == 0) {
                    Integer integer = map.get(userid1);
                    if (integer != null) {
                        shareData.setPostCount((long) integer);
                    }
                    Integer integer2 = downloadMap.get(userid1);
                    if (integer2 != null) {
                        shareData.setResumeDownload((long) integer2);
                    }
                }
                Long aLong = accessTime.get(userid1);
                if (aLong != null && aLong != 0) {
                    //访问平均时长
                    shareData.setSecond(aLong / visitTimes.get(userid1));
                }
                //分享的推荐官
                shareData.setRecommend(userid);
                //访问量
                shareData.setVisit((long) visitTimes.get(userid1));
                list.add(shareData);
                shareData.setCreateTime(LocalDateTime.now());
                //删除
                //  accessTime.remove(userid1);
                //   ip.remove(userid1);
                map.remove(userid1);
                downloadMap.remove(userid1);
            }
            //判断分享
            if (map.size() > 0) {
         /*       Set<String> strings1 = map.keySet();
                Iterator<String> iterator2 = strings1.iterator();
                while (iterator2.hasNext()) {
                    String next = iterator2.next();
                    ShareData shareData = new ShareData();
                    Map map1 = JSON.parseObject(next, Map.class);
                    shareData.setCity(map1.get("city").toString());
                    shareData.setProvince(map1.get("province").toString());
                    int type = Integer.parseInt(map1.get("type").toString());
                    //访问类型
                    shareData.setType(type);
                    shareData.setPostCount((long) map.get(next));
                    shareData.setCreateTime(LocalDateTime.now());
                    list.add(shareData);
                }*/
                List<ShareData> utils = utils(map,userid);
                list.addAll(utils);
            }
            if(downloadMap.size()>0){
                List<ShareData> shareData = downloadUtils(downloadMap,userid);
                list.addAll(shareData);
            }
            if(insertJobHunterMap.size()>0){
                List<ShareData> shareData = insertUser(insertJobHunterMap,userid);
                list.addAll(shareData);
            }
            Set<Integer> integers = inviteLinkMap.keySet();
            if(integers.size()>0){
                Iterator<Integer> iterator2 = integers.iterator();
                while (iterator2.hasNext()){
                    Integer next = iterator2.next();
                    Integer integer = inviteLinkMap.get(next);
                    ShareData shareData = new ShareData();
                    shareData.setPostCount(Long.valueOf(integer));
                    shareData.setType(next);
                    shareData.setRecommend(userid);
                    shareData.setCreateTime(LocalDateTime.now());
                    list.add(shareData);
                }
            }
            shareDataService.saveBatch(list);
            String[] a = {ShareDataEnums.SD_POST_SHARING.getShareData() + userid + dateStr,
                    ShareDataEnums.SD_USER_ID.getShareData()+dateStr,
                    ShareDataEnums.SD_POST_SHARING_RECOMMEND.getShareData() + userid + dateStr,
                    ShareDataEnums.INVITE_LINK.getShareData() + userid + dateStr,
                    ShareDataEnums.INSERT_USER.getShareData() + userid + dateStr,
                    ShareDataEnums.DOWNLOAD_POST.getShareData() + userid + dateStr};
            redisUtils.del(a);
        }

    }

    /**
     * 处理未对应的省市的map记录
     *
     * @param map
     * @return
     */
    public List<ShareData> utils(Map<String, Integer> map,String userId) {
        List<ShareData> list = new ArrayList(map.size());
        Set<String> strings1 = map.keySet();
        Iterator<String> iterator2 = strings1.iterator();
        while (iterator2.hasNext()) {
            String next = iterator2.next();
            ShareData shareData = new ShareData();
            Map map1 = JSON.parseObject(next, Map.class);
            shareData.setCity(map1.get("city").toString());
            shareData.setProvince(map1.get("province").toString());
            int type = Integer.parseInt(map1.get("type").toString());
            //访问类型
            shareData.setType(type);
            shareData.setRecommend(userId);
            shareData.setPostCount((long) map.get(next));
            shareData.setCreateTime(LocalDateTime.now());
            list.add(shareData);
        }
        return list;
    }

    public List<ShareData> downloadUtils(Map<String, Integer> map,String userId) {
        List<ShareData> list = new ArrayList(map.size());
        Set<String> strings1 = map.keySet();
        Iterator<String> iterator2 = strings1.iterator();
        while (iterator2.hasNext()) {
            String next = iterator2.next();
            ShareData shareData = new ShareData();
            Map map1 = JSON.parseObject(next, Map.class);
            shareData.setCity(map1.get("city").toString());
            shareData.setProvince(map1.get("province").toString());
            int type = Integer.parseInt(map1.get("type").toString());
            //访问类型
            shareData.setType(type);
            shareData.setRecommend(userId);
            //添加职位下载
            shareData.setRegister((long) map.get(next));
            shareData.setCreateTime(LocalDateTime.now());
            list.add(shareData);
        }
        return list;
    }
    public List<ShareData> insertUser(Map<String, Integer> map,String userId) {
        List<ShareData> list = new ArrayList(map.size());
        Set<String> strings1 = map.keySet();
        Iterator<String> iterator2 = strings1.iterator();
        while (iterator2.hasNext()) {
            String next = iterator2.next();
            ShareData shareData = new ShareData();
            Map map1 = JSON.parseObject(next, Map.class);
            shareData.setCity(map1.get("city").toString());
            shareData.setProvince(map1.get("province").toString());
            int type = Integer.parseInt(map1.get("type").toString());
            //访问类型
            shareData.setType(type);
            shareData.setRecommend(userId);
            shareData.setResumeDownload((long) map.get(next));
            shareData.setCreateTime(LocalDateTime.now());
            list.add(shareData);
        }
        return list;
    }
    public Map<String, Integer> statistics(List<Object> list) {
        Map<String, Integer> map = new ConcurrentHashMap(list.size());//存储求职者注册数
        for (Object insertJobHunterObject : list) {

            ShareDataPO ShareDataPO = JSON.parseObject(insertJobHunterObject.toString(), ShareDataPO.class);
            Map map1 = new HashMap(3);
            map1.put("province", ShareDataPO.getProvince());
            map1.put("city", ShareDataPO.getCity());
            map1.put("type", ShareDataPO.getType());
            String s = JSON.toJSONString(map1);
            Integer integer = map.get(s);
            if (integer == null) {
                integer = 0;
            }
            integer = integer + 1;
            map.put(s, integer);
        }
        return map;
    }
}
