package com.wcwy.websocket.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * websocket 通讯的消息类型
 *
 * @author 乐天
 * @since 2018-10-07
 */
@ApiModel(value = "通讯的消息类型")
@Data
public class Message implements Serializable {
    /**
     * 扩展字段，格式化为json
     */
    @ApiModelProperty(" 扩展字段，格式化为json")
    private String title;
    /**
     * 消息id
     */
    @ApiModelProperty("消息id")
    private String id;

    /**
     * 消息来源用户名
     */
    @ApiModelProperty("消息来源用户名")
    private String name;

    /**
     * 发送者头像
     */
    @ApiModelProperty("发送者头像")
    private String avatar;

    /**
     * 消息的来源ID（如果是私聊，则是用户id，如果是群聊，则是群组id）
     */
    @ApiModelProperty("消息的来源ID（如果是私聊，则是用户id，如果是群聊，则是群组id）")
    private String chatId;

    /**
     * 消息类型 friend|group
     */
    @ApiModelProperty("消息类型")
    private String type;

    /**
     * 消息类型 文本|附件|其他
     */
    @ApiModelProperty(" 消息类型 文本|附件|其他")
    private String messageType;

    /**
     * 消息内容
     */
    @ApiModelProperty(" 消息内容")
    private String content;

    /**
     * 是否本人发送
     */
    @ApiModelProperty(" 是否本人发送")
    private boolean mine;

    /**
     * 消息的发送者id
     */
    @ApiModelProperty(" 消息的发送者id")
    private String fromId;

    /**
     * 服务端时间戳毫秒数
     */
    @ApiModelProperty(" 服务端时间戳毫秒数")
    private Long timestamp;

    /**
     * 扩展字段，格式化为json
     */
    @ApiModelProperty(" 扩展字段，格式化为json")
    private Object extend;

    @ApiModelProperty("路由")
    private String router;
    @ApiModelProperty("身份(0:企业 1:推荐官 2:求职者)")
    private String identity;
    /**
     * 是否是离线的消息
     */
    private boolean offline = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Object getExtend() {
        return extend;
    }

    public void setExtend(Object extend) {
        this.extend = extend;
    }
}
