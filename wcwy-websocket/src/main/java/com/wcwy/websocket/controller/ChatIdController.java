package com.wcwy.websocket.controller;

import com.wcwy.common.base.result.R;
import com.wcwy.websocket.entity.ChatId;
import com.wcwy.websocket.service.ChatIdService;
import com.wcwy.websocket.session.CompanyMetadata;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: ChatIdController
 * Description:
 * date: 2023/12/30 11:20
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/chatId")
@Api(tags = "聊天id")
public class ChatIdController {
    @Autowired
    private ChatIdService chatIdService;
    @Autowired
    private CompanyMetadata companyMetadata;

    @GetMapping("/selectId")
    @ApiOperation("获取自己的聊天id")
    public R<ChatId> selectId(){
        ChatId userId = chatIdService.getUserId(companyMetadata.userid());
        return R.success(userId);
    }
}
