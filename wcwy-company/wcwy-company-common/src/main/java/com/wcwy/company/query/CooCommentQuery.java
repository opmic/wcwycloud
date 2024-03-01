package com.wcwy.company.query;

import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ClassName: CooCommentQuery
 * Description:
 * date: 2024/1/19 14:33
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("评论获取")
public class CooCommentQuery  extends PageQuery {

    @ApiModelProperty("帖子id")
    private Long id;

}
