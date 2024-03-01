package com.wcwy.company.po;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ClassName: TJobhunterEducationRecordPO
 * Description:
 * date: 2023/4/11 16:38
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@ApiModel(value = "学历基本信息")
@Data
public class TJobhunterEducationRecordPO {
    /**
     * 学校名称
     */

    @ApiModelProperty(value = "学校名称")
    private String shcoolName;


    /**
     * 学历
     */
    @ApiModelProperty(value = "学历")
    private String education;
}
