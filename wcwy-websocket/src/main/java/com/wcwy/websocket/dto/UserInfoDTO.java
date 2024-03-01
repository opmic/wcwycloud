package com.wcwy.websocket.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ClassName: UserInfo
 * Description:
 * date: 2023/10/25 10:54
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("用户基本信息")
public class UserInfoDTO {
    @ApiModelProperty("用户id")
    private String id;

    @ApiModelProperty("用户姓名")
    private String userName;


    @ApiModelProperty("头像")
    private String avatar;




}
