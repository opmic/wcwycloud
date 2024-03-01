package com.wcwy.post.pojo;

import com.wcwy.company.entity.CollerctPost;
import com.wcwy.post.po.TCompanyPostPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 岗位收藏表
 * @TableName collerct_post
 */

@Data
@ApiModel("岗位收藏表")
public class CollerctPostPOJO implements Serializable {
    /**
     * 岗位收藏id
     */
    @ApiModelProperty("岗位收藏id")
    private String collerctPostId;
    /**
     * 收藏的岗位
     */
    @ApiModelProperty("收藏的岗位")
    private List<CollerctPost> posts;

    /**
     * 收藏岗位用户id
     */
    @ApiModelProperty("收藏岗位用户id")
    private String collerctUserId;


    @ApiModelProperty("岗位信息")
    private TCompanyPostPO tCompanyPostPO;

}