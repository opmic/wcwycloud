package com.wcwy.system.annotation;

import com.wcwy.system.enums.BusinessType;
import com.wcwy.system.enums.OperatorType;

import java.lang.annotation.*;

/**
 * ClassName: RocketLog
 * Description:
 * date: 2023/6/27 14:03
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RocketLog {
    /**
     * 模块
     */
    public String title() default "";

    /**
     * 功能
     */
    public int businessType() default 2;

    /**
     * 操作人类别
     */
   /* public OperatorType operatorType() default OperatorType.MANAGE;*/

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    public boolean isSaveResponseData() default true;

    /**
     * 排除指定的请求参数
     */
    public String[] excludeParamNames() default {};
}
