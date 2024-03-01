package com.wcwy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 操作日志记录
 * @TableName sys_rocketmq_log
 */
@TableName(value ="sys_rocketmq_log")
@Data

public class SysRocketmqLog implements Serializable {
    /**
     * 日志主键
     */
    @TableId(value = "oper_id", type = IdType.AUTO)
    private Long operId;

    /**
     * 模块标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 业务类型（0生产 1消费）
     */
    @TableField(value = "business_type")
    private Integer businessType;

    /**
     * 方法名称
     */
    @TableField(value = "method")
    private String method;

    /**
     * 请求方式
     */
    @TableField(value = "request_method")
    private String requestMethod;

    /**
     * 操作人员
     */
    @TableField(value = "oper_name")
    private String operName;

    /**
     * 操作地点
     */
    @TableField(value = "oper_location")
    private String operLocation;

    /**
     * 请求参数
     */
    @TableField(value = "oper_param")
    private String operParam;

    /**
     * 返回参数
     */
    @TableField(value = "json_result")
    private String jsonResult;

    /**
     * 操作状态（0正常 1异常）
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 穿件时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 错误消息
     */
    @TableField(value = "error_msg")
    private String errorMsg;

    /**
     * 操作时间
     */
    @TableField(value = "oper_time")
    private LocalDateTime operTime;

    /**
     * 消耗时间
     */
    @TableField(value = "cost_time")
    private Long costTime;

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
        SysRocketmqLog other = (SysRocketmqLog) that;
        return (this.getOperId() == null ? other.getOperId() == null : this.getOperId().equals(other.getOperId()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getBusinessType() == null ? other.getBusinessType() == null : this.getBusinessType().equals(other.getBusinessType()))
            && (this.getMethod() == null ? other.getMethod() == null : this.getMethod().equals(other.getMethod()))
            && (this.getRequestMethod() == null ? other.getRequestMethod() == null : this.getRequestMethod().equals(other.getRequestMethod()))
            && (this.getOperName() == null ? other.getOperName() == null : this.getOperName().equals(other.getOperName()))
            && (this.getOperLocation() == null ? other.getOperLocation() == null : this.getOperLocation().equals(other.getOperLocation()))
            && (this.getOperParam() == null ? other.getOperParam() == null : this.getOperParam().equals(other.getOperParam()))
            && (this.getJsonResult() == null ? other.getJsonResult() == null : this.getJsonResult().equals(other.getJsonResult()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getErrorMsg() == null ? other.getErrorMsg() == null : this.getErrorMsg().equals(other.getErrorMsg()))
            && (this.getOperTime() == null ? other.getOperTime() == null : this.getOperTime().equals(other.getOperTime()))
            && (this.getCostTime() == null ? other.getCostTime() == null : this.getCostTime().equals(other.getCostTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getOperId() == null) ? 0 : getOperId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getBusinessType() == null) ? 0 : getBusinessType().hashCode());
        result = prime * result + ((getMethod() == null) ? 0 : getMethod().hashCode());
        result = prime * result + ((getRequestMethod() == null) ? 0 : getRequestMethod().hashCode());
        result = prime * result + ((getOperName() == null) ? 0 : getOperName().hashCode());
        result = prime * result + ((getOperLocation() == null) ? 0 : getOperLocation().hashCode());
        result = prime * result + ((getOperParam() == null) ? 0 : getOperParam().hashCode());
        result = prime * result + ((getJsonResult() == null) ? 0 : getJsonResult().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getErrorMsg() == null) ? 0 : getErrorMsg().hashCode());
        result = prime * result + ((getOperTime() == null) ? 0 : getOperTime().hashCode());
        result = prime * result + ((getCostTime() == null) ? 0 : getCostTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", 日志主键=").append(operId);
        sb.append(", 模块标题=").append(title);
        sb.append(", 业务类型（0生产 1消费）=").append(businessType);
        sb.append(", 方法名称=").append(method);
        sb.append(", 请求方式=").append(requestMethod);
        sb.append(", 操作人员=").append(operName);
        sb.append(", 操作地点=").append(operLocation);
        sb.append(", 请求参数=").append(operParam);
        sb.append(", 返回参数=").append(jsonResult);
        sb.append(", 异常=").append(status);
        sb.append(", 发生时间时间=").append(createTime);
        sb.append(", 错误消息=").append(errorMsg);
        sb.append(", operTime=").append(operTime);
        sb.append(", costTime=").append(costTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}