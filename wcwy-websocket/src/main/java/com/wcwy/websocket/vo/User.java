package com.wcwy.websocket.vo;

import com.wcwy.websocket.entity.Message;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
@ApiModel(value = "聊天用户信息")
public class User implements Serializable {
    @ApiModelProperty(value = "聊天id")
    private String id;
    @ApiModelProperty(value = "用户名字")
    private String name;
    @ApiModelProperty(value = "头像")
    private String avatar;
    @ApiModelProperty(value = "电话")
    private String mobile;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "身份类型(0：私信 1:通知 2:举报)")
    private Integer type;

    private String deptId;
    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "消息")
    private Message message;
    private List<User> friends;
    public User(String id, String name, String avatar, String mobile, String sex,String chatId) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.mobile = mobile;
        this.sex = sex;
        this.userId = chatId;
    }

    public User() {
    }
}
