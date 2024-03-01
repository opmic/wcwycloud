package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.company.entity.TJobhunterEducationRecord;
import com.wcwy.company.entity.TJobhunterExpectPosition;
import com.wcwy.company.entity.TJobhunterProjectRecord;
import com.wcwy.company.entity.TJobhunterWorkRecord;
import com.wcwy.company.po.CityPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * ClassName: CompanyTJobHunterResumeDTO
 * Description:
 * date: 2023/4/7 19:23
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("查看投简求职者信息")
public class CompanyTJobHunterResumeDTO {
    /**
     * 优势亮点
     */
    @ApiModelProperty("优势亮点")
    private List<String> advantage;

    @ApiModelProperty(value ="用户身份(1职场精英,2校园人才)" )
    private String userType;
    @ApiModelProperty(value ="邮箱" )
    private String email;

    @ApiModelProperty(value ="现住地址" )
    private CityPO address;
    @ApiModelProperty(value ="政治面貌(1:中共党员 2中共预备党员 3:共青团员 4:群众 5 民主党派)" )
    private Integer politicsStatus;
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

    @ApiModelProperty(value ="学历" )
    private String education;
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
    @ApiModelProperty(value ="是的代投(0:不是 1:是)" )
    private  Integer easco;
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
     * 求职状态(1:实习 2:应届生 3:在职暂无跳槽 4:在职急求机会 5:在职看机会 6:离职看机会)
     */
    @ApiModelProperty(value ="求职状态(1:离校-随时到岗(离职-随时到岗) 2:在校-月内到岗(在职-月内到岗) 3:在校-考虑机会(在职-考虑机会) 4:在校-暂不考虑(在职-暂不考虑))" )
    private String jobStatus;

    /**
     * 联系电话
     */
    @TableField(value = "phone")
    @ApiModelProperty(value ="联系电话" )
    private String phone;
    /**
     * 简历附件路径
     */
    @ApiModelProperty(value ="简历附件路径" )
    private String resumePath;

    @ApiModelProperty("投简说明")
    private String explains;

    @ApiModelProperty(value = "期望职位")
    private List<TJobhunterExpectPosition> position;

    @ApiModelProperty(value = "工作经历")
    private List<TJobhunterWorkRecord> record;

    @ApiModelProperty("项目经历")
    private List<TJobhunterProjectRecord> projectRecord;

    @ApiModelProperty("教育经历")
    private List<TJobhunterEducationRecord> educationRecord;

    @ApiModelProperty("是否下载")
    private Boolean download;
    @ApiModelProperty("是否收藏")
    private Boolean collect;
    @ApiModelProperty("收藏Id")
    private String collectId;
    @ApiModelProperty("简历id")
    private String putInResumeId;
    @ApiModelProperty("投放的岗位")
    private String putInPost;
    @ApiModelProperty(value ="目前年薪" )
    private BigDecimal currentSalary;

    @ApiModelProperty(value ="无忧币" )
    private Object currencyCount;

    @ApiModelProperty(value ="金币" )
    private Object gold;

    /**
     * 岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付岗位  4:简历付校园 5:简历付职位)
     */
    @ApiModelProperty(value ="岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付岗位  4:简历付校园 5:简历付职位)" )
    private Integer postType;
}
