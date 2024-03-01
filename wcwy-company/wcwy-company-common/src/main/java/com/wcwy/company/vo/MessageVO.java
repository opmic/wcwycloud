package com.wcwy.company.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * ClassName: MessageVO
 * Description:
 * date: 2023/7/25 16:00
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("消息体")
public class MessageVO {
    @ApiModelProperty("发送给谁")
    private Integer code;
    @ApiModelProperty("消息")
    /**
     * 信息的json 根据不同的消息类型分别解析
     */
    private Map<String, Object> message;

    /*
    *
    *
    * {
    "code": "1",
    "message": {
        "messageType": "0",
        "chatId": "5",
        "fromId": "1",
        "mine": true,
        "timestamp": 1703552499059,
        "content": "调休假阿达",
        "type": "0",
        "extend":{"url":"http://192.168.1.9:8082/profile/upload/2023/12/28/eef37f89-b1ed-43c1-bbf4-4fb1c0151ad6.pdf","fileName":"网才无忧营业执照副本2022.9.16.pdf"}
    }
}
    * */
}
