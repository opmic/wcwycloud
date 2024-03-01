package com.wcwy.company.po;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;

import java.time.LocalDate;

/**
 * ClassName: ShareDataPO
 * Description:
 * date: 2023/8/25 16:32
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
public class ShareDataPO {
    /**
     * 访问类型(0职位分享 1求职者 2猎企 3企业 4职场推荐官 5校园推荐官)
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
