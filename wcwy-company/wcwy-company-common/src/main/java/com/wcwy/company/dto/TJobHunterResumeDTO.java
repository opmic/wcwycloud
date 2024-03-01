package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.company.entity.TJobhunterEducationRecord;
import com.wcwy.company.entity.TJobhunterExpectPosition;
import com.wcwy.company.entity.TJobhunterProjectRecord;
import com.wcwy.company.entity.TJobhunterWorkRecord;
import com.wcwy.company.po.CityPO;
import com.wcwy.company.po.TJobhunterPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * ClassName: TJobHunterResumeDTO
 * Description:
 * date: 2022/12/29 10:51
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("求职者详细信息")
public class TJobHunterResumeDTO {
    /**
     * 简历id
     */
    @ApiModelProperty("简历id")
    private String resumeId;

    /**
     * 优势亮点
     */
    @ApiModelProperty("优势亮点")
    private List<String> advantage;



    @ApiModelProperty(value = "期望职位")
    private List<TJobhunterExpectPosition> tJobhunterExpectPosition;

    @ApiModelProperty(value = "工作经历")
    private List<TJobhunterWorkRecord> tJobhunterWorkRecord;

    @ApiModelProperty("项目经历")
    private List<TJobhunterProjectRecord> tJobhunterProjectRecord;

    @ApiModelProperty("教育经历")
    private List<TJobhunterEducationRecord> tJobhunterEducationRecord;

    /**
     * 用户ID
     */
    /**
     * 生日
     */
    @TableField(value = "birthday")
    @ApiModelProperty(value ="生日" )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @ApiModelProperty(value ="用户ID")
    private String userId;
    /**
     * 参加工作时间
     */
    @ApiModelProperty(value ="参加工作时间" )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate workTime;

    /**
     * 用户姓名
     */
    @ApiModelProperty(value ="用户姓名" )
    private String userName;


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
    @ApiModelProperty(value ="是否显示先生/女士（0不显示 1:显示）" )
    private Integer showSex;
    /**
     * 政治面貌(1:中共党员 2中共预备党员 3:共青团员 4:群众 5 民主党派)
     */
    @ApiModelProperty(value ="政治面貌(1:中共党员 2中共预备党员 3:共青团员 4:群众 5 民主党派)" )
    private Integer politicsStatus;
    /**
     * 求职状态(1:实习 2:应届生 3:在职暂无跳槽 4:在职急求机会 5:在职看机会 6:离职看机会)
     */
    @ApiModelProperty(value ="求职状态(1:实习 2:应届生 3:在职暂无跳槽 4:在职急求机会 5:在职看机会 6:离职看机会)" )
    private String jobStatus;

    /**
     * 用户身份z
     */
    @ApiModelProperty(value ="用户身份(职场人,:应届生,在校生)" )
    private String userType;

    /**
     * 目前年薪
     */
    @ApiModelProperty(value ="目前年薪" )
    private BigDecimal currentSalary;
    @ApiModelProperty(value ="是否隐藏或显示目前年薪(0:显示 1:隐藏)" )
    private Integer showCurrentSalary;


    /**
     * 年龄
     */
/*    @ApiModelProperty(value ="年龄" )
    private Integer age;*/
    /**
     * 联系电话
     */
    @TableField(value = "phone")
    @ApiModelProperty(value ="联系电话" )
    private String phone;


    /**
     * 微信号
     */
    @ApiModelProperty(value ="微信号" )
    private String wechatNumber;

    /**
     * 邮箱
     */
    @ApiModelProperty(value ="邮箱" )
    private String email;


    /**
     * 简历附件路径
     */
    @ApiModelProperty(value ="简历附件路径" )
    private String resumePath;

    /**
     * 审核状态(0待审核1审核通过2审核不通过)
     */
    @ApiModelProperty(value ="审核状态(0待审核1审核通过2审核不通过)" )
    private Integer examineStatus;

    /**
     * 分享注册人
     */
    @ApiModelProperty(value ="分享注册人" )
    private String sharePerson;

}
