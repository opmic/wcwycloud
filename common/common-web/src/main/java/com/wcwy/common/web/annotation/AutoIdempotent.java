package com.wcwy.common.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: AutoIdempotent
 * Description:确保唯一性
 * date: 2023/7/3 9:25
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoIdempotent {

}
