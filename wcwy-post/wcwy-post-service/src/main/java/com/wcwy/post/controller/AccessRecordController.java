package com.wcwy.post.controller;

import com.wcwy.common.base.utils.IpUtils;
import com.wcwy.post.asyn.AccessRecordAsync;
import com.wcwy.system.service.AccessRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * ClassName: AccessRecordServiceController
 * Description:
 * date: 2023/7/31 10:00
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "访问记录接口")
@RestController
@RequestMapping("/accessRecord")
public class AccessRecordController {

    @Autowired
    private AccessRecordAsync accessRecordAsync;

    @GetMapping("/save")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", required = true, value = "访问类型(0:企业 1推荐官 2求职者 3 首页)"),
            @ApiImplicitParam(name = "second", required = true, value = "访问时间(秒)")
    })
    @ApiOperation("添加访问记录")
    public String save(HttpServletRequest request, @RequestParam("type") Integer type, @RequestParam("second") Long second) throws IOException {
        String ipAddr = IpUtils.getIpAddr(request);
        accessRecordAsync.addAccessRecord(ipAddr,type,second);
        return ipAddr;
    }
}
