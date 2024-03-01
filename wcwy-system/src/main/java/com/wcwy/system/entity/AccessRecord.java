package com.wcwy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName access_record
 */
@TableName(value ="access_record")
@Data
public class AccessRecord implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 访问类型(0:企业 1推荐官 2求职者 3 首页)
     */
    @TableField(value = "type")
    private String type;

    /**
     * ip地址
     */
    @TableField(value = "ip_address")
    private Long ipAddress;

    /**
     * 访问时间(秒)
     */
    @TableField(value = "second")
    private Long second;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 访问量
     */
    @TableField(value = "visit")
    private Long visit;

    /**
     * 注册量
     */
    @TableField(value = "register")
    private Long register;

    /**
     * 省
     */
    @TableField(value = "province")
    private String province;

    /**
     * 市
     */
    @TableField(value = "city")
    private String city;

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
        AccessRecord other = (AccessRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getIpAddress() == null ? other.getIpAddress() == null : this.getIpAddress().equals(other.getIpAddress()))
            && (this.getSecond() == null ? other.getSecond() == null : this.getSecond().equals(other.getSecond()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getVisit() == null ? other.getVisit() == null : this.getVisit().equals(other.getVisit()))
            && (this.getRegister() == null ? other.getRegister() == null : this.getRegister().equals(other.getRegister()))
            && (this.getProvince() == null ? other.getProvince() == null : this.getProvince().equals(other.getProvince()))
            && (this.getCity() == null ? other.getCity() == null : this.getCity().equals(other.getCity()));
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
        result = prime * result + ((getVisit() == null) ? 0 : getVisit().hashCode());
        result = prime * result + ((getRegister() == null) ? 0 : getRegister().hashCode());
        result = prime * result + ((getProvince() == null) ? 0 : getProvince().hashCode());
        result = prime * result + ((getCity() == null) ? 0 : getCity().hashCode());
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
        sb.append(", visit=").append(visit);
        sb.append(", register=").append(register);
        sb.append(", province=").append(province);
        sb.append(", city=").append(city);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}