package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * ClassName: RmJobHunterDTO
 * Description:
 * date: 2023/5/26 10:32
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "推荐官获取求职者详情")
public class RmJobHunterDTO {
    /**
     * 用户ID
     */
    @ApiModelProperty(value ="求职者id")
    private String userId;

    /**
     * 用户姓名
     */
    @ApiModelProperty(value ="用户姓名" )
    private String userName;
    /**
     * 现住地址
     */

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
     * 用户性别（0男 1女 2未知）
     */
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
     * 用户身份(职场人,:应届生,在校生)
     */
    @ApiModelProperty(value ="用户身份(职场人,:应届生,在校生)" )
    private String userType;

    /**
     * 学历
     */
    @ApiModelProperty(value ="学历" )
    private String education;

    /**
     * 是否隐藏或显示目前年薪(0:显示 1:隐藏)
     */
    @ApiModelProperty(value ="是否隐藏或显示目前年薪(0:显示 1:隐藏)" )
    private Integer showCurrentSalary;

    /**
     * 目前年薪
     */
    @ApiModelProperty(value ="目前年薪" )
    private BigDecimal currentSalary;

    /**
     * 期望年薪
     */
    @ApiModelProperty(value ="期望年薪" )
    private BigDecimal expectSalary;

    /**
     * 联系电话
     */
    @ApiModelProperty(value ="联系电话" )
    private String phone;
    /**
     * 生日
     */
    @ApiModelProperty(value ="生日" )
    private LocalDate birthday;

    /**
     * 参加工作时间
     */
    @ApiModelProperty(value ="参加工作时间" )
    private LocalDate workTime;

    /**
     * 微信号
     */
    @ApiModelProperty(value ="微信号" )
    private String wechatNumber;



    /**
     * 邮箱
     */
    @TableField(value = "email")
    @ApiModelProperty(value ="邮箱" )
    private String email;

    /**
     * 个人优势
     */

    @ApiModelProperty("优势亮点")
    private List<String> advantage;

    /**
     * 附件地址
     */
    @TableField(value = "path")
    @ApiModelProperty(value = "附件地址")
    private List<String> path;




    @ApiModelProperty(value = "期望职位")
    private List<TJobhunterExpectPosition> tJobhunterExpectPosition;

    @ApiModelProperty(value = "工作经历")
    private List<TJobhunterWorkRecord> tJobhunterWorkRecord;

    @ApiModelProperty("项目经历")
    private List<TJobhunterProjectRecord> tJobhunterProjectRecord;

    @ApiModelProperty("教育经历")
    private List<TJobhunterEducationRecord> tJobhunterEducationRecord;

/*    @ApiModelProperty("求职者用户表")
    private TJobhunterPO tJobhunterPO;*/

    /**
     * 推荐官id
     */
    @ApiModelProperty(value = "人才来源(0:分享引流 1:人才委托 2新增人才 3应聘简历)")
    private Integer correlationType;
    @ApiModelProperty("简历id")
    private String resumeId;

    @ApiModelProperty(value = "是否下载(0:未下载 1:已下载)")
    private Integer downloadIf;

    @ApiModelProperty(value = "推荐人记录id")
    private Long referrerRecordId;
}
