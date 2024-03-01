package com.wcwy.company.asyn;

import com.alibaba.fastjson.JSON;
import com.wcwy.common.base.enums.ShareDataEnums;
import com.wcwy.common.base.utils.DateUtils;
import com.wcwy.common.base.utils.StringUtils;
import com.wcwy.common.base.vo.IpLocation;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.entity.EiCompanyPost;
import com.wcwy.company.entity.ShareData;
import com.wcwy.company.po.ProvincesCitiesPO;
import com.wcwy.company.po.ShareDataPO;
import com.wcwy.company.service.EiCompanyPostService;
import com.wcwy.post.vo.AccessRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.wcwy.common.base.utils.IpUtils.getLocation;

/**
 * ClassName: ShareDataAsync
 * Description:
 * date: 2023/8/25 15:52
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@Slf4j
public class ShareDataAsync {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private EiCompanyPostService eiCompanyPostService;


    /**
     *
     * @param ipAddr IP地址
     * @param type type:0职位分享 1求职者 2猎企 3企业 4职场推荐官 5校园推荐官
     * @param second
     * @throws IOException
     */
/*    @Async
    public void addShareData(String ipAddr, Integer type, Long second) throws IOException {

        IpLocation location = getLocation(ipAddr);
        if(location ==null){
            return;
        }
        if(StringUtils.isEmpty(location.getProvince())){
            return;
        }
        AccessRecordVO accessRecordVO = new AccessRecordVO();
        accessRecordVO.setSecond(second);
        accessRecordVO.setType(type);
        accessRecordVO.setCreateTime(LocalDate.now());
        accessRecordVO.setCity(location.getCity());
        accessRecordVO.setProvince(location.getProvince());
        accessRecordVO.setIpAddress(location.getIp());
        String currentDateStr = DateUtils.getCurrentDateStr(DateUtils.DATE_PATTERN_YYMMDDHH);
        redisUtils.lSet("AR"+currentDateStr, JSON.toJSONString(accessRecordVO),0);
    }*/

    /**
     * 分享岗位记录
     *
     * @param province 省
     * @param city     市
     *                 userid:推荐官id
     * @throws IOException
     */
    @Async
    public void shareDataPost(String province, String city, String userid) {
        //存储推荐官信息

        Map map = new HashMap(3);
        map.put("province", province);
        map.put("city", city);
        map.put("type",0);
        String currentDateStr = DateUtils.getCurrentDateStr(DateUtils.DATE_PATTERN_YYMMDDHH);
        redisUtils.sSet(ShareDataEnums.SD_USER_ID.getShareData()+currentDateStr, userid);
        redisUtils.lSet(ShareDataEnums.SD_POST_SHARING.getShareData() + userid + currentDateStr, JSON.toJSONString(map));
    }


    //分享邀请链接
    @Async
    public void inviteLink( String userid,int type) {
     /*   IpLocation location = null;
        try {
            location = getLocation(ipAddr);
        } catch (IOException e) {
            log.error("获取ip地址异常" + e);
            e.printStackTrace();
        }
        if (location == null) {
            return;
        }
        if (StringUtils.isEmpty(location.getProvince())) {
            return;
        }*/

        ShareDataPO shareData = new ShareDataPO();
        shareData.setType(type);
/*        shareData.setCity(location.getCity());
        shareData.setProvince(location.getProvince());
        shareData.setIpAddress(location.getIp());*/
        String currentDateStr = DateUtils.getCurrentDateStr(DateUtils.DATE_PATTERN_YYMMDDHH);
        redisUtils.sSet(ShareDataEnums.SD_USER_ID.getShareData()+currentDateStr, userid);
        redisUtils.lSet(ShareDataEnums.INVITE_LINK.getShareData() + userid + currentDateStr, JSON.toJSONString(shareData));
    }

    /**
     * @param ipAddr     IP地址
     * @param type:0职位分享 1求职者 2猎企 3企业 4职场推荐官 5校园推荐官
     * @param second     访问时长
     * @param userID     推荐官id
     * @throws IOException
     */
    @Async
    public void postSharingRecommend(String ipAddr, Integer type, Long second, String userID) {

        IpLocation location = null;
        try {
            location = getLocation(ipAddr);
        } catch (IOException e) {
            log.error("获取ip地址异常" + e);
            e.printStackTrace();
        }
        if (location == null) {
            return;
        }
        if (StringUtils.isEmpty(location.getProvince())) {
            return;
        }

        ShareDataPO shareData = new ShareDataPO();
        shareData.setSecond(second);
        shareData.setType(type);
        shareData.setCity(location.getCity());
        shareData.setProvince(location.getProvince());
        shareData.setIpAddress(location.getIp());
        String currentDateStr = DateUtils.getCurrentDateStr(DateUtils.DATE_PATTERN_YYMMDDHH);
        redisUtils.sSet(ShareDataEnums.SD_USER_ID.getShareData()+currentDateStr, userID);
        redisUtils.lSet(ShareDataEnums.SD_POST_SHARING_RECOMMEND.getShareData() + userID + currentDateStr, JSON.toJSONString(shareData), 0);

    }

    /**
     * 记录职位下载数据量
     * @param postId
     */

    @Async
    public void shareDataDownload(String postId,String userid){
        String substring = userid.substring(0, 2);
        if(! "TR".equals(substring)){
            return;
        }
        EiCompanyPost byId = eiCompanyPostService.getById(postId);
        if(byId !=null){
            ProvincesCitiesPO workCity = byId.getWorkCity();
            if(workCity !=null){
                Map map = new HashMap(3);
                map.put("province", workCity.getProvince());
                map.put("city", workCity.getCity());
                map.put("type",0);
                String currentDateStr = DateUtils.getCurrentDateStr(DateUtils.DATE_PATTERN_YYMMDDHH);
                redisUtils.sSet(ShareDataEnums.SD_USER_ID.getShareData()+currentDateStr, userid);
                redisUtils.lSet(ShareDataEnums.DOWNLOAD_POST.getShareData() + userid + currentDateStr, JSON.toJSONString(map));
            }

        }
    }
}
