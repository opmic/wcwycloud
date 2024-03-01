package com.wcwy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

/**
 * 前端错误
 * @TableName leading_end_mistake
 */
@TableName(value ="leading_end_mistake",autoResultMap = true)
@Data
public class LeadingEndMistake implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 错误
     */
    @TableField(value = "mistake",typeHandler = JacksonTypeHandler.class)
    private List<Object> mistake;

    /**
     * 路由
     */
    @TableField(value = "route")
    private String route;

    /**
     * 路由名
     */
    @TableField(value = "route_name")
    private String routeName;

    /**
     * 访问地址
     */
    @TableField(value = "visit_path")
    private String visitPath;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

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
        LeadingEndMistake other = (LeadingEndMistake) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMistake() == null ? other.getMistake() == null : this.getMistake().equals(other.getMistake()))
            && (this.getRoute() == null ? other.getRoute() == null : this.getRoute().equals(other.getRoute()))
            && (this.getRouteName() == null ? other.getRouteName() == null : this.getRouteName().equals(other.getRouteName()))
            && (this.getVisitPath() == null ? other.getVisitPath() == null : this.getVisitPath().equals(other.getVisitPath()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMistake() == null) ? 0 : getMistake().hashCode());
        result = prime * result + ((getRoute() == null) ? 0 : getRoute().hashCode());
        result = prime * result + ((getRouteName() == null) ? 0 : getRouteName().hashCode());
        result = prime * result + ((getVisitPath() == null) ? 0 : getVisitPath().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", mistake=").append(mistake);
        sb.append(", route=").append(route);
        sb.append(", routeName=").append(routeName);
        sb.append(", visitPath=").append(visitPath);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}