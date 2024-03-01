package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.wcwy.company.entity.TJobhunterEducationRecord;
import com.wcwy.company.entity.TJobhunterExpectPosition;
import com.wcwy.company.entity.TJobhunterWorkRecord;
import com.wcwy.company.po.CityPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: RMDownloadDTO
 * Description:
 * date: 2023/6/5 8:38
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@ApiModel(value = "推荐官我的下载")
@Data
public class RMDownloadDTO {
    /**
     * 用户ID
     */
    @ApiModelProperty(value ="用户ID")
    private String userId;



    /**
     * 用户姓名
     */
    @ApiModelProperty(value ="用户姓名" )
    private String userName;

    /**
     * 头像路径
     */
    @ApiModelProperty(value ="头像路径" )
    private String avatar;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @ApiModelProperty(value ="用户性别（0男 1女 2未知）" )
    private Integer sex;
    /**
     * 生日
     */
    @ApiModelProperty(value ="生日" )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    @ApiModelProperty(value = "下载时间")
    private LocalDateTime downloadTime;
    /**
     * 参加工作时间
     */
    @ApiModelProperty(value ="参加工作时间" )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate workTime;


    @ApiModelProperty(value ="教育经历表" )
    private List<TJobhunterEducationRecord> tJobhunterEducationRecord;


    @ApiModelProperty(value = "工作经历")
    private List<TJobhunterWorkRecord> tJobhunterWorkRecords;

    @ApiModelProperty(value = "求职期望")
    private List<TJobhunterExpectPosition> tJobhunterExpectPositions;
}
