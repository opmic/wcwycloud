package com.wcwy.post.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * ClassName: TCompanyPostStopVO
 * Description:
 * date: 2022/9/16 14:30
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "取消+停止+开始招聘岗位实体类")
public class TCompanyPostStopVO {
    /**
     * 发布岗位Id
     */
    @ApiModelProperty(value = "发布岗位Id")
    @NotEmpty(message = "发布岗位Id不能为空")
    private List<String> postId;

    @ApiModelProperty(value = "岗位状态(0：停止招聘1：招聘中：2：取消岗位)")
    @NotEmpty(message = "岗位状态不能为空")
    private Integer status;
}
