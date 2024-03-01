package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理员账号

 * @TableName admin_user
 */
@Data
@ApiModel("管理员账号账号信息及权限")
public class AdminUserRoles {
    /**
     * 管理员id
     */
    @ApiModelProperty(value = "管理员id")
    private String adminId;

    /**
     * 管理员名字
     */
    @ApiModelProperty(value = "管理员名字")
    private String adminName;

    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    private String phone;

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    private String loginName;

    @ApiModelProperty(value = "权限")
    private List<String> roles;
}