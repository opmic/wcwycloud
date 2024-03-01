package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ClassName: TPositionVo
 * Description:
 * date: 2022/9/2 14:43
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value ="职位表" )
public class TPositionVo {
    /**
     * 职位id
     */
    @ApiModelProperty(value = "职位id")
    private Integer positionId;

    /**
     * 职位
     */
    @ApiModelProperty(value="职位")
    private String position;

}
