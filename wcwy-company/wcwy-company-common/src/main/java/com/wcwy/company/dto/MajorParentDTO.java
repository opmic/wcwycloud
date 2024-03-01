package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wcwy.company.entity.Major;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * ClassName: MajorParebtDTO
 * Description:
 * date: 2022/9/7 15:46
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "专业名称表")
public class MajorParentDTO {
    /**
     * 专业代码
     */
    @ApiModelProperty(value = "专业代码")
    private String majoCode;

    /**
     * 专业名称
     */
    @ApiModelProperty(value = "专业名称")
    private String majorName;

    /**
     * 专业状态
     */
    @ApiModelProperty(value = "专业状态")
    private Integer majorState;

    /**
     * 父类id
     */
    @ApiModelProperty(value = "父类id")
    private String parentId;

    @ApiModelProperty(value = "专业名称表")
    private List<MajorDTO> majorList;
}
