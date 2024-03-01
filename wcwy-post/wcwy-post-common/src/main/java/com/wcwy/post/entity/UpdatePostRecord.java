package com.wcwy.post.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName update_post_record
 */
@TableName(value ="update_post_record")
@Data
public class UpdatePostRecord implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 职位id
     */
    @TableField(value = "post_id")
    private String postId;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 更新人员
     */
    @TableField(value = "update_id")
    private String updateId;

    /**
     * 历史数据
     */
    @TableField(value = "historical_data")
    private Object historicalData;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}