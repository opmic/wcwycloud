package com.wcwy.websocket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 消息
 * @TableName chat_news
 */
@TableName(value ="chat_news")
@Data
@ApiModel("消息")
public class ChatNews implements Serializable {
    /**
     * 聊天id
     */
    @TableId(value = "news_id",type = IdType.AUTO)
    @ApiModelProperty("聊天id")
    private Long newsId;

    /**
     * 聊天室id
     */
    @TableField(value = "chat_id")
    @ApiModelProperty(" 聊天室id")
    private String chatId;

    /**
     * 接收人
     */
    @TableField(value = "to_id")
    @ApiModelProperty(" 接收人")
    private String toId;

    /**
     * 发送人id
     */
    @TableField(value = "from_id")
    @ApiModelProperty("发送人id")
    private String fromId;

    /**
     * 发送时间
     */
    @TableField(value = "send_time")
    @ApiModelProperty("发送时间")
    private LocalDateTime sendTime;

    /**
     * 类型(0单聊1:群聊)
     */
    @TableField(value = "type")
    @ApiModelProperty("类型(0单聊1:群聊)")
    private Integer type;

    /**
     * 1:已读 0:未读
     */
    @TableField(value = "read_status")
    @ApiModelProperty("1:已读 0:未读")
    private Integer readStatus;

    /**
     * 消息内容
     */
    @TableField(value = "msg")
    @ApiModelProperty("消息内容")
    private String msg;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ChatNews other = (ChatNews) that;
        return (this.getNewsId() == null ? other.getNewsId() == null : this.getNewsId().equals(other.getNewsId()))
            && (this.getChatId() == null ? other.getChatId() == null : this.getChatId().equals(other.getChatId()))
            && (this.getToId() == null ? other.getToId() == null : this.getToId().equals(other.getToId()))
            && (this.getFromId() == null ? other.getFromId() == null : this.getFromId().equals(other.getFromId()))
            && (this.getSendTime() == null ? other.getSendTime() == null : this.getSendTime().equals(other.getSendTime()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getReadStatus() == null ? other.getReadStatus() == null : this.getReadStatus().equals(other.getReadStatus()))
            && (this.getMsg() == null ? other.getMsg() == null : this.getMsg().equals(other.getMsg()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getNewsId() == null) ? 0 : getNewsId().hashCode());
        result = prime * result + ((getChatId() == null) ? 0 : getChatId().hashCode());
        result = prime * result + ((getToId() == null) ? 0 : getToId().hashCode());
        result = prime * result + ((getFromId() == null) ? 0 : getFromId().hashCode());
        result = prime * result + ((getSendTime() == null) ? 0 : getSendTime().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getReadStatus() == null) ? 0 : getReadStatus().hashCode());
        result = prime * result + ((getMsg() == null) ? 0 : getMsg().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", newsId=").append(newsId);
        sb.append(", chatId=").append(chatId);
        sb.append(", toId=").append(toId);
        sb.append(", fromId=").append(fromId);
        sb.append(", sendTime=").append(sendTime);
        sb.append(", type=").append(type);
        sb.append(", readStatus=").append(readStatus);
        sb.append(", msg=").append(msg);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}