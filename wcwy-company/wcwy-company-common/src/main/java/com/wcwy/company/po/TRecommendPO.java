package com.wcwy.company.po;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ClassName: TRecommendPO
 * Description:
 * date: 2022/9/8 11:30
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "推荐官表")
public class TRecommendPO {

    /**
     * id
     */
    @ApiModelProperty(value ="id" )
    private String id;

    /**
     * 姓名
     */
    @ApiModelProperty(value ="姓名" )
    private String username;

    @ApiModelProperty(value ="企业logo" )
    private String logo;


    @ApiModelProperty(value ="头像地址" )
    private String headPath;

    /**
     * 出生年月日
     */

    @ApiModelProperty(value ="出生年月日" )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birth;

    /**
     * 学历
     */
    @ApiModelProperty(value ="学历" )
    private String education;

    /**
     * 性别:(1:男 2:女生)
     */
    @ApiModelProperty(value ="性别:(1:男 2:女生)" )
    private Integer sex;

    /**
     * 电话号码
     */
    @ApiModelProperty(value ="电话号码" )
    private String phone;

    /**
     * 身份证
     */
    @ApiModelProperty(value ="身份证" )
    private String card;

    /**
     * 微信号
     */
    @ApiModelProperty(value ="微信号" )
    private String wechatId;

    /**
     * 身份证正面
     */
    @ApiModelProperty(value ="身份证正面" )
    private String cardFront;

    /**
     * 身份证反面
     */
    @ApiModelProperty(value ="身份证反面" )
    private String cardVerso;


    /**
     * 自我介绍
     */
    @ApiModelProperty(value ="自我介绍" )
    private String manMessage;

    /**
     * 所在院校
     */
    @ApiModelProperty(value ="所在院校" )
    private String academy;

    /**
     * 专业
     */
    @ApiModelProperty(value ="专业" )
    private String careerId;

    /**
     * 入学时间
     */
    @ApiModelProperty(value ="入学时间" )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate entranceTime;

    /**
     *  毕业时间
     */
    @ApiModelProperty(value ="毕业时间" )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate graduateTime;

    /**
     * 教育经历
     */
    @ApiModelProperty(value ="是否统招(0:统招 1:非统招)" )
    private Integer recruitment;

    /**
     * 身份(1:职场人 2：应届生 3：在校生)
     */
    @ApiModelProperty(value ="身份(1:职场人 2：应届生 3：在校生 4:企业推荐官)" )
    private Integer identity;

    @ApiModelProperty(value = "企业id")
    private String companyId;
    /**
     * 等级
     */
    @ApiModelProperty(value ="等级" )
    private String gradeId;

    /**
     * 经验值
     */
    @ApiModelProperty(value ="经验值" )
    private Long empiricalValue;

    /**
     * 等级证书
     */
    @ApiModelProperty(value ="等级证书" )
    private String certificate;

    /**
     * 链接分享人
     */
    @ApiModelProperty(value ="链接分享人" )
    private String sharePerson;

    /**
     * 审核状态(0待审核1审核中2通过3未通过)
     */
    @ApiModelProperty(value ="审核状态(0待审核1审核中2通过3未通过)" )
    private Integer examineStatus;

    /**
     * 审核建议
     */
    @ApiModelProperty(value ="审核建议" )
    private String examineResult;


    /**
     * 退出时间
     */

    @ApiModelProperty(value ="退出时间" )
    private LocalDateTime logoutTime;

    /**
     * 登录时间
     */

    @ApiModelProperty(value ="登录时间" )
    private LocalDateTime loginTime;

    @ApiModelProperty(value = "所住地")
    private ProvincesCitiesPO address;






}
