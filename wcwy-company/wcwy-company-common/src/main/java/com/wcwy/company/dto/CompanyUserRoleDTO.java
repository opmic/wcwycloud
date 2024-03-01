package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.company.po.TJobhunterEducationRecordPO;
import com.wcwy.company.po.TJobhunterWorkRecordPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 企业查看用户简历权限表
 * @TableName company_user_role
 */
@Data
@ApiModel("下载的求职者")
public class CompanyUserRoleDTO  {
    /**
     * 用户ID
     */
    @ApiModelProperty(value ="用户ID")
    private String userId;
    @ApiModelProperty("简历id")
    private String resumeId;

    @ApiModelProperty(value ="联系电话" )
    private String phone;

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
    /**
     * 公司名称
     */
    @ApiModelProperty(value ="公司名称")
    private String companyName;

    /**
     * 职位名称
     */
    @ApiModelProperty(value ="职位名称")
    private String positionName;


    @ApiModelProperty(value = "学校名称")
    private String shcoolName;


    /**
     * 学历
     */
    @ApiModelProperty(value = "学历")
    private String education;

    @ApiModelProperty(value = "是否下载(0已收藏 1:未收藏)")
    public Integer collect;
}