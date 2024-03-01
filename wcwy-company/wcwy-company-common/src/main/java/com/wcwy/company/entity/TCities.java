package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 城市信息表
 * @TableName t_cities
 */
@TableName(value ="t_cities")
@Data
@ApiModel(value = "城市信息表")
@AllArgsConstructor
@NoArgsConstructor
public class TCities implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Integer id;

    /**
     * 城市编码
     */
    @TableField(value = "cityid")
    @ApiModelProperty(value = "城市编码")
    private String cityid;

    /**
     * 城市名称
     */
    @TableField(value = "city")
    @ApiModelProperty(value = "城市编码")
    private String city;

    /**
     * 所属省份编码
     */
    @TableField(value = "provinceid")
    @ApiModelProperty(value = "所属省份编码")
    private String provinceid;



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
        TCities other = (TCities) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCityid() == null ? other.getCityid() == null : this.getCityid().equals(other.getCityid()))
            && (this.getCity() == null ? other.getCity() == null : this.getCity().equals(other.getCity()))
            && (this.getProvinceid() == null ? other.getProvinceid() == null : this.getProvinceid().equals(other.getProvinceid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCityid() == null) ? 0 : getCityid().hashCode());
        result = prime * result + ((getCity() == null) ? 0 : getCity().hashCode());
        result = prime * result + ((getProvinceid() == null) ? 0 : getProvinceid().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", cityid=").append(cityid);
        sb.append(", city=").append(city);
        sb.append(", provinceid=").append(provinceid);
        sb.append("]");
        return sb.toString();
    }
}