package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ClassName: StationDTO
 * Description:
 * date: 2022/9/6 15:08
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "岗位表")
public class StationDTO {
    /**
     * 职位id
     */
    @ApiModelProperty(value = "职位id")
    private Long code;

    /**
     * 职位标签
     */
    @ApiModelProperty(value = "职位标签")
    private String name;
}
