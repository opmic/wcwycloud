package com.wcwy.company.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * ClassName: TJobhunterWorkRecordPO
 * Description:
 * date: 2023/4/11 16:39
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "工作经历表基本数据")
public class TJobhunterWorkRecordPO {
    /**
     * 公司名称
     */
    @ApiModelProperty(value ="公司名称")
    private String companyName;

    /**
     * 职位名称
     */
    @ApiModelProperty(value ="职位名称")
    private String positionName;

}
