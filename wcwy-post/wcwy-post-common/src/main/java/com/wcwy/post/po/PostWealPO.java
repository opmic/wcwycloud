package com.wcwy.post.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * ClassName: PostWealPO
 * Description:
 * date: 2022/9/14 16:21
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
public class PostWealPO {

    /**
     * 岗位福利id
     */
    @ApiModelProperty(value = "岗位福利id")
    private Long wealId;

    /**
     * 岗位福利
     */
    @ApiModelProperty("岗位福利")
    @NotBlank(message = "岗位福利不能为空！")
    private String weal;
}
