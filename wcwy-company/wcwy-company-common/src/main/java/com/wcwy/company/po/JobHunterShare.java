package com.wcwy.company.po;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ClassName: JobHunterShare
 * Description:
 * date: 2023/4/10 15:40
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "分享管理求职者")
public class JobHunterShare {
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
    @TableField(value = "address",typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value ="现住地址" )
    private CityPO address;
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
     * 学历
     */
    @ApiModelProperty(value ="学历" )
    private String education;

    /**
     * 创建时间
     */
    @ApiModelProperty(value ="创建时间" )
    private LocalDateTime createTime;

    @ApiModelProperty(value ="生日" )
    private LocalDate birthday;

    @ApiModelProperty(value ="参加工作时间" )
    private LocalDate workTime;

    @ApiModelProperty(value = "下载次数")
    private Long download=0L;

    @ApiModelProperty(value = "下载总费用")
    private BigDecimal cost=new BigDecimal(0);
    @ApiModelProperty(value = "收益")
    private BigDecimal earnings=new BigDecimal(0);
    @ApiModelProperty(value = "引入来源(0:用户注册推广 1:职位推广)")
    private Integer origin;


    private Long referrerRecordId;

    @ApiModelProperty(value = "是否删除")
    private Boolean isDelete;
    @ApiModelProperty(value = "登录时间")
    private Object loginTime;

    @ApiModelProperty(value = "登录时长")
    private long loginTimeSum;
}
