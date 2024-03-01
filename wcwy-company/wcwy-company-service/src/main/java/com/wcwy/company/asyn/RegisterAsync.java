package com.wcwy.company.asyn;

import com.alibaba.fastjson.JSON;

import com.wcwy.common.base.enums.ShareDataEnums;
import com.wcwy.common.base.utils.DateUtils;
import com.wcwy.common.base.utils.StringUtils;
import com.wcwy.common.base.vo.IpLocation;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.po.ShareDataPO;
import com.wcwy.post.vo.AccessRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;


import static com.wcwy.common.base.utils.IpUtils.getLocation;

/**
 * ClassName: RegisterAsync
 * Description:
 * date: 2023/8/1 15:25
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Slf4j
@Component
public class RegisterAsync {

    @Autowired
    private RedisUtils redisUtils;

    @Async
    public void register(String ipAddr, Integer type) throws IOException {
        IpLocation location = getLocation(ipAddr);
        if (location == null) {
            return;
        }
        if (StringUtils.isEmpty(location.getProvince())) {
            return;
        }
        AccessRecordVO accessRecordVO = new AccessRecordVO();
        accessRecordVO.setRegister(1);
        accessRecordVO.setType(type);
        accessRecordVO.setCreateTime(LocalDate.now());
        accessRecordVO.setCity(location.getCity());
        accessRecordVO.setProvince(location.getProvince());
        accessRecordVO.setIpAddress(location.getIp());
        String currentDateStr = DateUtils.getCurrentDateStr(DateUtils.DATE_PATTERN_YYMMDDHH);
        redisUtils.lSet("RS" + currentDateStr, JSON.toJSONString(accessRecordVO), 0);
    }

    /**
     * 注册缓存记录
     * @param ipAddr
     * @param i
     * @param recommend
     */
    @Async
    public void register(String ipAddr, int i, String recommend) {
        log.info("执行缓存注册数据111111111111111111。。。。。。。。。。。。。。。。");
        IpLocation location = null;
        try {
            location = getLocation(ipAddr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (location == null) {
            return;
        }
        if (StringUtils.isEmpty(location.getProvince())) {
            return;
        }
        ShareDataPO shareData = new ShareDataPO();
        shareData.setRegister(1);
        shareData.setType(i);
        shareData.setCreateTime(LocalDate.now());
        shareData.setCity(location.getCity());
        shareData.setProvince(location.getProvince());
        shareData.setIpAddress(location.getIp());
        String currentDateStr = DateUtils.getCurrentDateStr(DateUtils.DATE_PATTERN_YYMMDDHH);
        log.info("执行缓存注册数据。。。。。。。。。。。。。。。。");
        redisUtils.lSet(ShareDataEnums.INSERT_USER.getShareData()+ recommend + currentDateStr, JSON.toJSONString(shareData), 0);
        redisUtils.sSet(ShareDataEnums.SD_USER_ID.getShareData()+currentDateStr, recommend);
    }
}
