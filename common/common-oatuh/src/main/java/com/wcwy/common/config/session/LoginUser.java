package com.wcwy.common.config.session;


import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author lmabbe
 * @data 2021/12/9 22:26
 */
@Data
@Accessors(chain = true)
public class LoginUser implements Serializable {

    /**
     * 用户ID
     */
    private String id;

    /**
     * 姓名
     */
    private String username;

    /**
     * 头像
     */
    private String userAvatar;

    /**
     * 手机
     */
    private String userMobile;

    /**
     * 邮箱
     */
    private String userEmail;

    /**
     * 性别 0.未知 1.男 2.女
     */
    private Integer userSex;

    /**
     * 权限列表
     */
    private Collection<? extends GrantedAuthority> authorities;
   /* private List<String> authorities;*/

    /**
     * 密码
     */
    private String password;

    /**
     * 密码错误次数
     */
    private Integer passwordErrorNum;

    /**
     * 是否有效(1.有效 0.无效 -1.软删除)
     */
    private Integer valid;


    /**
     * 自定义数据权限
     */
    private List<Integer> customDataScope;

    /**
     * 是否授权(0:停用,1:启用)
     */
    private Integer serviceProvidersAuthorization;
}
