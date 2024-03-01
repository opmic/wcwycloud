package com.wcwy.post.asyn;

import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.wcwy.common.base.utils.DateUtils;
import com.wcwy.common.base.utils.IpUtils;
import com.wcwy.common.base.utils.StringUtils;
import com.wcwy.common.base.vo.IpLocation;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.post.vo.AccessRecordVO;
import com.wcwy.system.entity.AccessRecord;
import com.wcwy.system.service.AccessRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import static com.wcwy.common.base.utils.IpUtils.getLocation;

/**
 * ClassName: AccessRecordAsyn
 * Description:
 * date: 2023/7/31 10:08
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Slf4j
@Component
public class AccessRecordAsync {

    @Autowired
    private AccessRecordService accessRecordService;

    @Autowired
    private RedisUtils redisUtils;

    @Async
    public void addAccessRecord(String ipAddr, Integer type, Long second) throws IOException {

        IpLocation location = getLocation(ipAddr);
        log.info(location.toString());
        log.info("11111111111111111111111111111111111111");
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
        redisUtils.lSet("AR"+currentDateStr,JSON.toJSONString(accessRecordVO),0);
    }





}
