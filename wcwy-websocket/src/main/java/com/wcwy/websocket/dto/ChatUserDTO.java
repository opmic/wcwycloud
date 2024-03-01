package com.wcwy.websocket.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wcwy.websocket.entity.ChatNews;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ClassName: ChatUserDTO
 * Description:
 * date: 2023/10/25 14:29
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("查询聊天列表")
public class ChatUserDTO {
    /**
     * 主键id
     */
    @ApiModelProperty("聊天室id")
    private String id;

    /**
     * 用户id
     */
    @ApiModelProperty("本人id")
    private String userId;

    /**
     * 对方id
     */

    @ApiModelProperty("对方id")
    private String anotherId;

    /**
     * 未读数量
     */
    @TableField(value = "unread")
    private Integer unread;
    /**
     * 状态(0:正常1:屏蔽)
     */
    @TableField(value = "state")
    private Integer state;

    @ApiModelProperty("对方详细信息")
    private UserInfoDTO userInfoDTO;

    @ApiModelProperty("最近一段聊天")
    private ChatNews chatNews;
}
