package com.wcwy.websocket.query;

import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * ClassName: ChatNewsQuery
 * Description:
 * date: 2023/10/26 15:17
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("查询求职者简历")
public class ChatNewsQuery extends PageQuery {
    @ApiModelProperty("聊天室id")
    @NotBlank(message = "聊天室id不能为空！")
    private String chatId;

}
