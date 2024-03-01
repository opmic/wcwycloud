package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.company.po.CityPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * ClassName: InsertJobHunterAppVO
 * Description:
 * date: 2023/8/23 9:20
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "求职者app端填写简历")
public class InsertJobHunterAppVO {
    /**
     * 用户姓名
     */

    @ApiModelProperty(value = "用户姓名")
    @NotBlank(message = "用户姓名不能为空")
    private String userName;
    /**
     * 现住地址
     */

    @ApiModelProperty(value = "现住地址")
    @Valid
    @NotNull(message = "现住地址信息不能为空")
    private CityPO address;

    /**
     * 用户性别（0男 1女 2未知）
     */

    @ApiModelProperty(value = "用户性别（0男 1女 2未知）")
    @NotNull(message = "用户性别信息不能为空")
    private Integer sex;

    /**
     * 联系电话
     */

    @ApiModelProperty(value = "联系电话")
    @NotNull(message = "联系电话不能为空")
    private String phone;
    /**
     * 用户性别（0男 1女 2未知）
     */
/*    @ApiModelProperty(value = "是否显示先生/女士（0不显示 1:显示）")
    @NotNull(message = "是否显示先生/女士不能为空")
    private Integer showSex;*/

    /**
     * 求职状态(1:实习 2:应届生 3:在职暂无跳槽 4:在职急求机会 5:在职看机会 6:离职看机会)
     */
    @ApiModelProperty(value = "求职状态(1:离校-随时到岗(离职-随时到岗) 2:在校-月内到岗(在职-月内到岗) 3:在校-考虑机会(在职-考虑机会) 4:在校-暂不考虑(在职-暂不考虑))")
    @NotBlank(message = "求职状态不为空")
    private String jobStatus;
    /**
     * 用户身份(职场人,:应届生,在校生)
     */
    @TableField(value = "user_type")
    @ApiModelProperty(value = "用户身份(1职场精英,2校园人才)")
    @NotBlank(message = "用户身份")
    private String userType;



/*    @ApiModelProperty(value = "政治面貌(1:中共党员 2中共预备党员 3:共青团员 4:群众 5 民主党派)")
    @NotNull(message = "政治面貌不能为空")
    private Integer politicsStatus;*/
    /**
     * 参加工作时间
     */
    @ApiModelProperty(value = "参加工作时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate workTime;
    /**
     * 目前年薪
     */
    @ApiModelProperty(value = "目前年薪(单位W)")
    @NotNull(message = "目前年薪不能为空！")
    private BigDecimal currentSalary;

    /**
     * 生日
     */
    @ApiModelProperty(value = "生日")
    @Past(message = "必须是一个过去的日期")
    @NotNull(message = "日期不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    /**
     * 是否隐藏或显示目前年薪(0:显示 1:隐藏)
     */
/*    @TableField(value = "show_current_salary")
    @ApiModelProperty(value = "是否隐藏或显示目前年薪(0:显示 1:隐藏)")
    @NotNull(message = "是否隐藏或显示目前年薪")
    private Integer showCurrentSalary;*/





    /**
     * 邮箱
     */
/*    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮箱不正确")
    private String email;*/


    /**
     * 简历附件路径
     */
  /*  @ApiModelProperty(value = "简历附件路径")
    private String resumePath;*/

    @ApiModelProperty("个人优势")
    @NotNull(message = "个人优势不能为空!")
    private List<String> advantage;
    @ApiModelProperty(value ="头像路径" )
    @NotBlank(message = "头像路径不能为空!")
    private String avatar;




    /**
     * 学校名称
     */

    @ApiModelProperty(value = "学校名称")
    @NotBlank(message = "学校名称不能为空")
    private String shcoolName;

    /**
     * 学制类型(0其他1全日制2非全日制)
     */
    @ApiModelProperty(value = "学制类型(0其他1全日制2非全日制)")
    @NotNull(message = "类型不能为空!")
    private Integer edulType;

    /**
     * 学历
     */
    @ApiModelProperty(value = "学历")
    @NotBlank(message = "学历不能为空")
    private String education;

    /**
     * 专业
     */
    @ApiModelProperty(value = "专业")
    @NotBlank(message = "专业不能为空")
    private String major;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "毕业开始时间")
    @NotBlank(message = "毕业开始时间不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "毕业结束时间")
    @NotBlank(message = "毕业结束时间不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String endTime;







//期望职位
    /**
     * 期望职位
     */
    @ApiModelProperty(value = "期望职位")
    @NotEmpty(message = "期望职位不能为空")
    private List<String> positionName;

    /**
     * 工作城市
     */
    @ApiModelProperty(value = "期望工作城市")
    @NotEmpty(message = "期望工作城市不能为空")
    private List<String> workCity;

    /**
     * 期望年薪
     */
    @ApiModelProperty(value = "最低期望年薪(单位:W)")
    @NotNull(message = "最低期望年薪不能为空")
    private BigDecimal expectSalary;

    @ApiModelProperty(value = "最高期望年薪")
    @NotNull(message = "最高期望年薪不能为空")
    private BigDecimal endExpectSalary;







    /**
     * 公司名称
     */
    @ApiModelProperty(value ="公司名称")
    //@NotBlank(message = "公司名称不能为空")
    @Size(message = "公司名称必须大于8个字符",min = 8,max = 20)
    private String companyName;
    @ApiModelProperty(value ="工作内容")
    //@NotBlank(message = "工作内容不能为空")
    @Size(message = "工作内容必须大于40个字符",min = 40)
    private String jobDescription;
    /**
     * 所属行业
     */
    @ApiModelProperty(value ="所属行业")
   // @NotNull(message = "所属行业不能为空")
    private List<String> industry;

    @ApiModelProperty(value ="职位类型")
    private String position;

    /**
     * 职位名称
     */
    @ApiModelProperty(value ="职位名称")
   // @NotBlank(message = "职位名称不能为空")
    private String workPositionName;

    /**
     * 在职开始时间
     */
    @ApiModelProperty(value ="在职开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    //@NotBlank(message = "在职开始时间不能为空")
    private String workStartTime;

    /**
     * 在职结束时间
     */
    @ApiModelProperty(value ="在职结束时间")
    //@NotBlank(message = "在职结束时间不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String workEndTime;

}
