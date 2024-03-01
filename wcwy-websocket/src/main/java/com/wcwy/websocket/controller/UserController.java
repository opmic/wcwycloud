package com.wcwy.websocket.controller;

import com.wcwy.websocket.service.VimUserApiService;
import com.wcwy.websocket.utils.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * ClassName: UserController
 * Description:
 * date: 2023/12/26 9:46
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/api/sys/users")
@Api(tags = "获取用户信息")
public class UserController {
    @Resource
    private VimUserApiService vimUserApiService;

    @GetMapping("{userId}")
    @ApiOperation("聊天id获取用户信息")
    @ApiImplicitParam(name = "userId", required = true, value = "聊天id")
    public AjaxResult get(@PathVariable String userId) {
        return AjaxResult.success(vimUserApiService.get(userId));
    }
}
