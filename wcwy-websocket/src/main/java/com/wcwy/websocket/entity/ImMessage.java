package com.wcwy.websocket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName im_message
 */
@TableName(value ="im_message")
@Data
public class ImMessage implements Serializable {
    /**
     * 
     */
    @TableId(value = "id")
    private String id;

    /**
     * 接收人
     */
    @TableField(value = "to_id")
    private String toId;

    /**
     * 发送人
     */
    @TableField(value = "from_id")
    private String fromId;

    /**
     * 发送时间
     */
    @TableField(value = "send_time")
    private Long sendTime;

    /**
     * 内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 扩展
     */
    @TableField(value = "extend")
    private String extend;

    /**
     * 类型
     */
    @TableField(value = "message_type")
    private String messageType;

    /**
     * 聊天室KEY
     */
    @TableField(value = "chat_key")
    private String chatKey;

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
        ImMessage other = (ImMessage) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getToId() == null ? other.getToId() == null : this.getToId().equals(other.getToId()))
            && (this.getFromId() == null ? other.getFromId() == null : this.getFromId().equals(other.getFromId()))
            && (this.getSendTime() == null ? other.getSendTime() == null : this.getSendTime().equals(other.getSendTime()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getExtend() == null ? other.getExtend() == null : this.getExtend().equals(other.getExtend()))
            && (this.getMessageType() == null ? other.getMessageType() == null : this.getMessageType().equals(other.getMessageType()))
            && (this.getChatKey() == null ? other.getChatKey() == null : this.getChatKey().equals(other.getChatKey()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getToId() == null) ? 0 : getToId().hashCode());
        result = prime * result + ((getFromId() == null) ? 0 : getFromId().hashCode());
        result = prime * result + ((getSendTime() == null) ? 0 : getSendTime().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getExtend() == null) ? 0 : getExtend().hashCode());
        result = prime * result + ((getMessageType() == null) ? 0 : getMessageType().hashCode());
        result = prime * result + ((getChatKey() == null) ? 0 : getChatKey().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", toId=").append(toId);
        sb.append(", fromId=").append(fromId);
        sb.append(", sendTime=").append(sendTime);
        sb.append(", content=").append(content);
        sb.append(", extend=").append(extend);
        sb.append(", messageType=").append(messageType);
        sb.append(", chatKey=").append(chatKey);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}