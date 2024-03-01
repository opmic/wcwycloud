package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

/**
 * 用户微信信息表
 * @TableName wechat_user
 */
@TableName(value ="wechat_user")
@Data
public class WechatUser implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    private String id;

    /**
     * 电话
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 普通用户的标识，对当前开发者帐号唯一
     */
    @TableField(value = "openid")
    private String openid;

    /**
     * 普通用户昵称
     */
    @TableField(value = "nickname")
    private String nickname;

    /**
     * 微信用户性别，1为男性，2为女性
     */
    @TableField(value = "sex")
    private Integer sex;

    /**
     * 省份
     */
    @TableField(value = "province")
    private String province;

    /**
     * 城市
     */
    @TableField(value = "city")
    private String city;

    /**
     * 国家，如中国为CN
     */
    @TableField(value = "country")
    private String country;

    /**
     * 头像
     */
    @TableField(value = "headimgurl")
    private String headimgurl;

    /**
     * 用户特权信息
     */
    @TableField(value = "privilege" ,typeHandler = JacksonTypeHandler.class)
    private String  privilege;

    /**
     * unionid是唯一的
     */
    @TableField(value = "unionid")
    private Integer unionid;

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
        WechatUser other = (WechatUser) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getOpenid() == null ? other.getOpenid() == null : this.getOpenid().equals(other.getOpenid()))
            && (this.getNickname() == null ? other.getNickname() == null : this.getNickname().equals(other.getNickname()))
            && (this.getSex() == null ? other.getSex() == null : this.getSex().equals(other.getSex()))
            && (this.getProvince() == null ? other.getProvince() == null : this.getProvince().equals(other.getProvince()))
            && (this.getCity() == null ? other.getCity() == null : this.getCity().equals(other.getCity()))
            && (this.getCountry() == null ? other.getCountry() == null : this.getCountry().equals(other.getCountry()))
            && (this.getHeadimgurl() == null ? other.getHeadimgurl() == null : this.getHeadimgurl().equals(other.getHeadimgurl()))
            && (this.getPrivilege() == null ? other.getPrivilege() == null : this.getPrivilege().equals(other.getPrivilege()))
            && (this.getUnionid() == null ? other.getUnionid() == null : this.getUnionid().equals(other.getUnionid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getOpenid() == null) ? 0 : getOpenid().hashCode());
        result = prime * result + ((getNickname() == null) ? 0 : getNickname().hashCode());
        result = prime * result + ((getSex() == null) ? 0 : getSex().hashCode());
        result = prime * result + ((getProvince() == null) ? 0 : getProvince().hashCode());
        result = prime * result + ((getCity() == null) ? 0 : getCity().hashCode());
        result = prime * result + ((getCountry() == null) ? 0 : getCountry().hashCode());
        result = prime * result + ((getHeadimgurl() == null) ? 0 : getHeadimgurl().hashCode());
        result = prime * result + ((getPrivilege() == null) ? 0 : getPrivilege().hashCode());
        result = prime * result + ((getUnionid() == null) ? 0 : getUnionid().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", phone=").append(phone);
        sb.append(", openid=").append(openid);
        sb.append(", nickname=").append(nickname);
        sb.append(", sex=").append(sex);
        sb.append(", province=").append(province);
        sb.append(", city=").append(city);
        sb.append(", country=").append(country);
        sb.append(", headimgurl=").append(headimgurl);
        sb.append(", privilege=").append(privilege);
        sb.append(", unionid=").append(unionid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}