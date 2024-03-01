package com.wcwy.websocket.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * ClassName: SendInfo
 * Description:
 * date: 2023/11/8 11:20
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
public class SendInfo implements Serializable {

    /**
     * 发送信息的代码
     */
    private String code;

    /**
     * 信息的json 根据不同的消息类型分别解析
     */
    private Map<String, Object> message;
}
