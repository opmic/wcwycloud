package com.wcwy.common.base.result;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author lmabbe
 * @data 2021/2/7 22:47
 */
@Slf4j
@Builder
@Accessors(chain = true)
@Data
@ApiModel(value = "返回实体类")
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {

    @ApiModelProperty(value = "返回代码")
    private Integer code;

    @ApiModelProperty(value = "提示信息")
    private String message;

    @ApiModelProperty(value = "数据")
    private T data;

    @ApiModelProperty(value = "扩展数据")
    private Map<String, Object> extra;

    @ApiModelProperty(value = "时间戳")
    private Long timestamp;

    public static R success() {
        return new RBuilder<>().code(200).message("操作成功!").timestamp(System.currentTimeMillis()).build();
    }

    public static <T> R success(T obj) {
        return new RBuilder<>().code(200).message("操作成功!").data(obj).timestamp(System.currentTimeMillis()).build();
    }
    public static <T> R success(String msg,T obj) {
        return new RBuilder<>().code(200).message(msg).data(obj).timestamp(System.currentTimeMillis()).build();
    }
    public static  R failed(IResultCode resultCode) {
        return new RBuilder<>().code(Integer.parseInt(resultCode.getCode())).message(resultCode.getMsg()).timestamp(System.currentTimeMillis()).build();
    }
    public static <T> R failed(String msg) {
        return new RBuilder<>().code(Integer.parseInt(ResultCode.SYSTEM_EXECUTION_ERROR.getCode())).message(msg).timestamp(System.currentTimeMillis()).build();
    }

    public static <T> R failed(IResultCode resultCode, String msg) {
        return new RBuilder<>().code(Integer.parseInt(resultCode.getCode())).message(msg).timestamp(System.currentTimeMillis()).build();
    }


    public static <T> R page(IPage<T> page) {
        Map map= new ConcurrentHashMap();
        map.put("total", page.getTotal());
        return new RBuilder<>().code(200).message("操作成功!").data(page.getRecords()).extra(map).timestamp(System.currentTimeMillis()).build();
    }

    public static R fail() {
        return fail("操作失败!");
    }

    public static R fail(String message) {
        return new RBuilder<>().code(405).message(message).timestamp(System.currentTimeMillis()).build();
    }
    public static <T> R fail(String msg,T obj) {
        return new RBuilder<>().code(405).message(msg).data(obj).timestamp(System.currentTimeMillis()).build();
    }

    public static R error() {
        return error(null);
    }

    public static R error(Integer code, String message) {
        return new RBuilder<>()
                .code(code)
                .message(StrUtil.isBlank(message) ? "error" : message)
                .build();
    }


    public static R error(String message) {
        return error(500, message);
    }



    /*    *//**
     * 运行结果其他属性
     *//*
    private Map<String, Object> props = Maps.newHashMap();

    *//**
     * 添加属性
     *
     * @param key
     * @param value
     * @return
     *//*
    public R<T> addProps(String key, Object value) {
        if (props == null) {
            this.props = Maps.newHashMap();
        }
        this.props.put(key, value);
        return this;
    }*/
}
