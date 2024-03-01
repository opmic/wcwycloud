package com.wcwy.common.base.query.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.wcwy.common.base.enums.WhereTypeEnum;
import com.wcwy.common.base.query.WhereFun;
import com.wcwy.common.base.query.WhereType;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lmabbe
 */
@Data
public class WhereEntityHandler {

    private static Map<WhereTypeEnum, WhereFun> typeFunc;

    static {
        if (typeFunc == null) {
            typeFunc = new HashMap<>();
            typeFunc.put(WhereTypeEnum.EQ, (w, k, v) -> {
                w.eq(k, v);
            });
            typeFunc.put(WhereTypeEnum.NEQ, (w, k, v) -> {
                w.ne(k, v);
            });
            typeFunc.put(WhereTypeEnum.IN, (w, k, v) -> {
                if (v instanceof Collection) {
                    w.in(k, (Collection<?>) v);
                } else if (v instanceof Object[]) {
                    w.in(k, (Object[]) v);
                } else {
                    w.in(k, v.toString());
                }
            });
            typeFunc.put(WhereTypeEnum.LIKE, (w, k, v) -> {
                w.like(k, v.toString());
            });
            typeFunc.put(WhereTypeEnum.LE, (w, k, v) -> {
                w.le(k, v);
            });
            typeFunc.put(WhereTypeEnum.LT, (w, k, v) -> {
                w.lt(k, v);
            });
            typeFunc.put(WhereTypeEnum.GE, (w, k, v) -> {
                w.ge(k, v);
            });
            typeFunc.put(WhereTypeEnum.GT, (w, k, v) -> {
                w.gt(k, v);
            });
            typeFunc.put(WhereTypeEnum.JSON_ARRAY_IN, (w, k, v) -> {
                w.apply("JSON_CONTAINS(" + k + ", CONCAT('\"',{0},'\"'))", v);
            });
        }

    }

    /**
     * 封装成需要的wrapper
     *
     * @param t 实体对象
     * @return
     */
    public static <T> QueryWrapper<T> invoke(Object t) {
        QueryWrapper<T> wrapper = new QueryWrapper();
        execute(t, wrapper);
        // 获取
        return wrapper;
    }

    public static <T> QueryWrapper<T> invoke(Object t, QueryWrapper<T> wrapper) {
        execute(t, wrapper);
        return wrapper;
    }

    /**
     * 执行
     *
     * @param t       obj
     * @param wrapper
     */
    public static void execute(Object t, QueryWrapper wrapper) {
        //反射获取属性
        Field[] fields = t.getClass().getDeclaredFields();
        for (int i=0;i<fields.length-1;i++){
            for (int j=0;j<fields.length-1-i;j++){
               Field fieldc=null;
                if (fields[j].getAnnotation(WhereType.class).sort()>fields[j+1].getAnnotation(WhereType.class).sort()) {
                    fieldc = fields[j];
                    fields[j] = fields[j+1];
                    fields[j+1] = fieldc;
                }
            }
        }
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object val = field.get(t);
                String colum = "";
                if (val != null && "FFFF".equals(val.toString())) {
                    val = "";
                }
                if (val != null && !"".equals(val.toString())) {
                    WhereType whereType = field.getAnnotation(WhereType.class);
                    //没有注解，取默认为下划线拼接
                    if (whereType == null) {
                        colum = camelToUnderline(field.getName());
                        // 执行方法
                        typeFunc.get(WhereTypeEnum.EQ).whereFunc(wrapper, colum, val);
                    } else {
                        if (whereType.ignore()) {
                            continue;
                        } else if (whereType.isSort() && val != null) {
                            //没有定义查询属性，取默认
                            if (!StrUtil.isBlank(whereType.field())) {
                                colum = whereType.field();
                            } else if (!StrUtil.isBlank(whereType.value())) {
                                colum = whereType.value();
                            } else {
                                colum = camelToUnderline(field.getName());
                            }
                            // 执行方法
                            if ("asc".equals(whereType.order())) {
                                wrapper.orderByAsc(colum);
                            } else if ("desc".equals(whereType.order())) {
                                wrapper.orderByDesc(colum);
                            }
                        } else {
                            //没有定义查询属性，取默认
                            if (!StrUtil.isBlank(whereType.field())) {
                                colum = whereType.field();
                            } else if (!StrUtil.isBlank(whereType.value())) {
                                colum = whereType.value();
                            } else {
                                colum = camelToUnderline(field.getName());
                            }
                            // 是否另取一个where
//                            if (whereType.andNew()) {
//                                wrapper.andNew();
//                            }
                            // 执行方法
                            typeFunc.get(whereType.type()).whereFunc(wrapper, colum, val);
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_");
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}