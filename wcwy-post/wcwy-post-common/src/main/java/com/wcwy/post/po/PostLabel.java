package com.wcwy.post.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ClassName: PostLabel
 * Description:
 * date: 2023/4/1 10:24
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "公司岗位名称")
public class PostLabel {

    @ApiModelProperty(value = "发布岗位Id")
    private String postId;
    @ApiModelProperty(value = "岗位名称")
    private String postLabel;


}
