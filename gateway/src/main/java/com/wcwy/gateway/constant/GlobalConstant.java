package com.wcwy.gateway.constant;

/**
 * 全局常量
 *
 * @author lmabbe
 */
@SuppressWarnings("ALL")
public interface GlobalConstant {

    /**
     * 是或者否
     */
    interface YES_OR_NO {
        Integer YES = 1;
        Integer NO = 0;
    }

    /**
     * 是否有效
     */
    interface IS_VALID {
        /**
         * 有效
         */
        Integer YES = 1;
        String YES_STR = "1";
        /**
         * 无效
         */
        Integer N0 = 0;
        String N0_STR = "0";
        /**
         * 删除
         */
        Integer DELETE = -1;
        String DELETE_STR = "-1";
    }


    /**
     * 系统信息
     */
    interface SYSTEM {

        String BASE_PACKAGE_NAME = "com.lmabbe";

        /**
         * 租户IDkey
         */
        String TENANT_ID = "tenant_id";

        /**
         * 认证信息前缀
         */
        String AUTHENTICATION_PREFIX = "Bearer ";

        /**
         * 认证信息存储的头key
         */
        String AUTHORIZATION = "Authorization";

        /**
         * 请求接口的客户端
         */
        String FROM_CLIENT = "from_client";

        /**
         * 登录的用户
         */
        String LOGIN_USER = "login-user";

        /**
         * 登录的用户
         */
        String JWT = "access_token";

    }
}
