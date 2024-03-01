package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * ClassName: TRecommendUpdateVO
 * Description:
 * date: 2022/9/9 10:26
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

@Data
@ApiModel(value = "推荐官修改信息实体类")
public class TRecommendUpdateVO {
    /**
     * 登录名(使用电话号码)
     */
    @NotBlank(message = "登录名不能为空")
    @ApiModelProperty(value ="登录名(使用电话号码)" )
    private String loginName;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名", required = true)
    @NotBlank(message = "姓名不能为空")
    private String username;


    @ApiModelProperty(value ="企业logo" )
    private String logo;

    @NotBlank(message = "头像地址不能为空")
    @ApiModelProperty(value ="头像地址" )
    private String headPath;
    /**
     * 出生年月日
     */
    @ApiModelProperty(value = "出生年月日")
    @NotNull(message = "出生年月日不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birth;

    /**
     * 学历
     */
    @ApiModelProperty(value = "学历")
    @NotBlank(message = "学历不能为空")

    private String education;

    /**
     * 性别:(1:男 2:女生)
     */
    @ApiModelProperty(value = "性别:(1:男 2:女生)")
    @NotNull(message = "性别不能为空")
    private Integer sex;

    /**
     * 电话号码
     */
    @ApiModelProperty(value = "电话号码")
    @NotBlank(message = "电话号码不能为空")
    private String phone;

    /**
     * 身份证
     */
    @ApiModelProperty(value = "身份证")
    @NotBlank(message = "身份证不能为空")
    private String card;

    /**
     * 微信号
     */
    @ApiModelProperty(value = "微信号")
    /*  @NotBlank(message = "微信号不能为空")*/
    private String wechatId;

    /**
     * 身份证正面
     */
    @ApiModelProperty(value = "身份证正面")
    @NotBlank(message = "身份证正面不能为空")
    private String cardFront;

    /**
     * 身份证反面
     */
    @ApiModelProperty(value = "身份证反面")
    @NotBlank(message = "身份证反面不能为空")
    private String cardVerso;


    /**
     * 自我介绍
     */
    @ApiModelProperty(value = "自我介绍")
    @NotBlank(message = "自我介绍不能为空")
    @Size(max = 300, min = 5, message = "自我介绍50-300")
    private String manMessage;

    /**
     * 所在院校
     */
    @ApiModelProperty(value = "所在院校")
    @NotBlank(message = "所在院校不能为空")
    private String academy;

    /**
     * 专业
     */
    @ApiModelProperty(value = "专业")
    @NotBlank(message = "专业不能为空")
    private String careerId;

    /**
     * 入学时间
     */
    @ApiModelProperty(value = "入学时间")
    @NotNull(message = "入学时间不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate entranceTime;

    /**
     * 毕业时间
     */
    @ApiModelProperty(value = "毕业时间")
    @NotNull(message = "毕业时间不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate graduateTime;

    /**
     * 教育经历
     */

    @ApiModelProperty(value = "是否统招(0:统招 1:非统招)")
    @NotNull(message = "统招不能为空")
    private Integer recruitment;

    /**
     * 身份(1:职场人 2：应届生 3：在校生)
     */

    @ApiModelProperty(value = "身份(1:职场人 2：在校生)")
    @NotNull(message = "身份不能为空")
    private Integer identity;


    @TableField(value = "company_id")
    @ApiModelProperty(value = "企业id")
    private String companyId;

    /**
     * 链接分享人
     */
    @ApiModelProperty(value = "链接分享人")
    private String sharePerson;
}
