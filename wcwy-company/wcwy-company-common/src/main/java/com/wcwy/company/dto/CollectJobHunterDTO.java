package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.company.po.CityPO;
import com.wcwy.company.po.TJobhunterEducationRecordPO;
import com.wcwy.company.po.TJobhunterWorkRecordPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: CollectJobHunterDTO
 * Description:
 * date: 2023/4/11 16:06
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("收藏的求职者")
public class CollectJobHunterDTO {
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

    /**
     * 参加工作时间
     */
    @ApiModelProperty(value ="参加工作时间" )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate workTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("收藏时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "工作经历表基本数据")
    TJobhunterWorkRecordPO tJobhunterWorkRecordPO;

    @ApiModelProperty(value = "学历基本信息")
    TJobhunterEducationRecordPO tJobhunterEducationRecordPO;

    @ApiModelProperty(value = "是否下载(0已下载 1:未下载)")
    public Integer download;
    @ApiModelProperty(value = "是否收藏(0:已收藏 1:未收藏)")
    private Integer isCollect=0;
}
