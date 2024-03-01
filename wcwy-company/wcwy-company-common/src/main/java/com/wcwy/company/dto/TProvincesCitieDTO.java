package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wcwy.company.entity.TCities;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ClassName: TProvincesCitieDTO
 * Description:
 * date: 2022/9/2 10:41
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TProvincesCitieDTO {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Integer id;

    /**
     * 省份编码
     */
    @TableField(value = "provinceid")
    @ApiModelProperty(value = "省份编码")
    private Integer provinceid;

    /**
     * 省份
     */
    @TableField(value = "province")
    @ApiModelProperty(value = "省份")
    private String province;

  /*  @TableField(value = "tCitiesList")*/
    @ApiModelProperty(value = "市表")
    List<TCities> list;
}
