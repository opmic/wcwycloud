package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分享运维表
 * @TableName share_data
 */
@TableName(value ="share_data")
@Data
@ApiModel("分享运维表")
public class ShareData implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    /**
     * 访问类型(0职位分享 1求职者 2猎企 3企业 4职场推荐官 5校园推荐官)
     */
    @TableField(value = "type")
    @ApiModelProperty("访问类型(0职位分享 1求职者 2猎企 3企业 4职场推荐官 5校园推荐官)")
    private Integer type;

    /**
     * ip地址
     */
    @TableField(value = "ip_address")
    @ApiModelProperty("ip地址")
    private Long ipAddress;

    /**
     * 访问时间(秒)
     */
    @TableField(value = "second")
    @ApiModelProperty("访问时间(秒)")
    private Long second;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 推荐官id
     */
    @TableField(value = "recommend")
    @ApiModelProperty("推荐官id")
    private String recommend;

    /**
     * 访问量
     */
    @TableField(value = "visit")
    @ApiModelProperty("访问量")
    private Long visit;

    /**
     * 注册量
     */
    @TableField(value = "register")
    @ApiModelProperty("注册量")
    private Long register;

    /**
     * 省
     */
    @TableField(value = "province")
    @ApiModelProperty("省")
    private String province;

    /**
     * 市
     */
    @TableField(value = "city")
    @ApiModelProperty("市")
    private String city;

    /**
     * 职位分享数
     */
    @TableField(value = "post_count")
    @ApiModelProperty("职位分享数")
    private Long postCount;

    /**
     * 职位下载数据
     */
    @TableField(value = "resume_download")
    @ApiModelProperty("职位下载数据")
    private Long resumeDownload;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ShareData other = (ShareData) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getIpAddress() == null ? other.getIpAddress() == null : this.getIpAddress().equals(other.getIpAddress()))
            && (this.getSecond() == null ? other.getSecond() == null : this.getSecond().equals(other.getSecond()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getRecommend() == null ? other.getRecommend() == null : this.getRecommend().equals(other.getRecommend()))
            && (this.getVisit() == null ? other.getVisit() == null : this.getVisit().equals(other.getVisit()))
            && (this.getRegister() == null ? other.getRegister() == null : this.getRegister().equals(other.getRegister()))
            && (this.getProvince() == null ? other.getProvince() == null : this.getProvince().equals(other.getProvince()))
            && (this.getCity() == null ? other.getCity() == null : this.getCity().equals(other.getCity()))
            && (this.getPostCount() == null ? other.getPostCount() == null : this.getPostCount().equals(other.getPostCount()))
            && (this.getResumeDownload() == null ? other.getResumeDownload() == null : this.getResumeDownload().equals(other.getResumeDownload()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getIpAddress() == null) ? 0 : getIpAddress().hashCode());
        result = prime * result + ((getSecond() == null) ? 0 : getSecond().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getRecommend() == null) ? 0 : getRecommend().hashCode());
        result = prime * result + ((getVisit() == null) ? 0 : getVisit().hashCode());
        result = prime * result + ((getRegister() == null) ? 0 : getRegister().hashCode());
        result = prime * result + ((getProvince() == null) ? 0 : getProvince().hashCode());
        result = prime * result + ((getCity() == null) ? 0 : getCity().hashCode());
        result = prime * result + ((getPostCount() == null) ? 0 : getPostCount().hashCode());
        result = prime * result + ((getResumeDownload() == null) ? 0 : getResumeDownload().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", ipAddress=").append(ipAddress);
        sb.append(", second=").append(second);
        sb.append(", createTime=").append(createTime);
        sb.append(", recommend=").append(recommend);
        sb.append(", visit=").append(visit);
        sb.append(", register=").append(register);
        sb.append(", province=").append(province);
        sb.append(", city=").append(city);
        sb.append(", postCount=").append(postCount);
        sb.append(", resumeDownload=").append(resumeDownload);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}