package com.wcwy.post.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.wcwy.post.entity.TCompanyPost;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javafx.concurrent.Worker;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * ClassName: HomeDTO
 * Description:
 * date: 2023/1/11 16:41
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("首页获取信息")
public class HomeDTO {
    @ApiModelProperty(value = "最新职位")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME,property = "mytype", include=JsonTypeInfo.As.PROPERTY)
    @JsonSubTypes({ //设置对应子类的识别码值
            @JsonSubTypes.Type(value = LinkedHashMap.class, name = "TCompanyPost"),
            @JsonSubTypes.Type(value = TCompanyPost.class, name = "TCompanyPost")
    })
    List<TCompanyPost> latestPost;
    @ApiModelProperty(value = "热招职位")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME,property = "mytype", include=JsonTypeInfo.As.PROPERTY)
    @JsonSubTypes({ //设置对应子类的识别码值
            @JsonSubTypes.Type(value = LinkedHashMap.class, name = "TCompanyPost"),
            @JsonSubTypes.Type(value = TCompanyPost.class, name = "TCompanyPost")
    })
    List<TCompanyPost> hotJob;
    @ApiModelProperty(value = "热门企业")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME,property = "mytype", include=JsonTypeInfo.As.PROPERTY)
    @JsonSubTypes({ //设置对应子类的识别码值
            @JsonSubTypes.Type(value = LinkedHashMap.class, name = "TCompanyHotPostDTO"),
            @JsonSubTypes.Type(value = TCompanyHotPostDTO.class, name = "TCompanyHotPostDTO")
    })
    List<TCompanyHotPostDTO> tCompanyHotPostDTOS;

}
