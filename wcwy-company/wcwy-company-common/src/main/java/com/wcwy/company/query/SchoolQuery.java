package com.wcwy.company.query;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * ClassName: SchoolQuery
 * Description:
 * date: 2022/9/8 16:06
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "学校查询实体类")
public class SchoolQuery {
   private String id;
   private String name;
}
