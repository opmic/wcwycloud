/**
 * Copyright (c) 2011-2014, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.wcwy.common.base.query;


import com.wcwy.common.base.enums.WhereTypeEnum;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 查询字段类型标识
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WhereType {

    /**
     * <p>
     * 查询类型
     * </p>
     */
    WhereTypeEnum type() default WhereTypeEnum.EQ;

    /**
     * 判定字段名
     *
     * @return
     */
    @AliasFor("field")
    String value() default "";

    /**
     * 判定字段名
     *
     * @return
     */
    @AliasFor("value")
    String field() default "";

    /**
     * 是否忽略
     */
    boolean ignore() default false;

    boolean andNew() default false;

    /**
     * 是否是排序字段
     * 只支持单字段
     */
    boolean isSort() default false;

    /**
     * isSort = true 的情况下 是倒序还是升序
     * 只支持单字段
     */
    String order() default "asc";

    /**
     * sort对字段进行排序
     *
     */

    int sort() default 20;

}
