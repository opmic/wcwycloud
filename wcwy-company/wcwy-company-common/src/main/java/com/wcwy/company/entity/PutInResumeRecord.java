package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wcwy.company.vo.PutInResumeRecordVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 投简记录
 * @TableName put_in_resume_record
 */
@TableName(value ="put_in_resume_record",autoResultMap = true)
@Data
@ApiModel(value = "投简记录")
public class PutInResumeRecord implements Serializable {
    /**
     * 内容
     */
    @TableField(value = "content",typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "内容(投放状态(0:投简1:浏览、 2:下载、3排除  4:约面、5:接受面试 6:不接受面试 7:面试修改  8:面试通过 9:淘汰 10:取消面试 11:offer、12:已入职、13:未入职))")
    private List<Object> content;

    /**
     * 投简id
     */
    @TableId(value = "put_in_resume_id")
    @ApiModelProperty(value = "投简id")
    private String putInResumeId;

}