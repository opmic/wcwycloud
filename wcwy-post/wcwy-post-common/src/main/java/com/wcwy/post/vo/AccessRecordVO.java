package com.wcwy.post.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;

import java.time.LocalDate;


/**
 * ClassName: AccessRecordVO
 * Description:
 * date: 2023/8/1 11:53
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
public class AccessRecordVO {
    /**
     * 访问类型(0:企业 1推荐官 2求职者 3 首页)
     */
    private Integer type;


    /**
     * 注册数量
     */
    private Integer register;
    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 访问时间(秒)
     */
    private Long second;

    /**
     * 创建时间
     */
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate createTime;


    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;
}
