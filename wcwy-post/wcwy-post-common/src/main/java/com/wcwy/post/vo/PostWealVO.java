package com.wcwy.post.vo;

import com.wcwy.post.entity.PostWeal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * ClassName: PostWealVO
 * Description:
 * date: 2022/9/14 16:13
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "岗位福利实体类")
public class PostWealVO {
    /**
     * 岗位福利id
     */

    @ApiModelProperty(value = "岗位福利id")
    private Long wealId;

    /**
     * 岗位福利
     */

    @ApiModelProperty("岗位福利")
    private String weal;

    /**
     * 主节点(0为主节点)
     */

    @ApiModelProperty("主节点(0为主节点)")
    private Long parebt;

    @ApiModelProperty("子福利")
   private List<PostWeal> list;
}
