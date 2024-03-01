package com.wcwy.oauth.json;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * json操作类
 *
 * @author lmabbe
 */
@Component
public class JsonHelper {

    private static ObjectMapper jacksonObjectMapper;

    public JsonHelper(ObjectMapper jacksonObjectMapper) {
        JsonHelper.jacksonObjectMapper = jacksonObjectMapper;
    }


    /**
     * 对象转json字符串
     *
     * @param obj 需要转成json的对象
     * @return {@link String}
     * @date 2022/5/31 17:28
     * @author liming
     */
    @SneakyThrows
    public static String toJsonStr(Object obj) {
        return jacksonObjectMapper.writeValueAsString(obj);
    }

    /**
     * json字符串转List
     *
     * @param json  json字符串
     * @param clazz List 对象类型
     * @return {@link List<T>}
     * @date 2022/5/31 17:29
     * @author liming
     */
    @SneakyThrows
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        JavaType javaType = jacksonObjectMapper.getTypeFactory().constructParametricType(List.class, clazz);
        return jacksonObjectMapper.readValue(json, javaType);
    }

    /**
     * json字符串转对象
     *
     * @param json  json字符串
     * @param clazz 对象类型
     * @return {@link T}
     * @date 2022/5/31 17:30
     * @author liming
     */
    @SneakyThrows
    public static <T> T parseObject(String json, Class<T> clazz) {
        return (T) jacksonObjectMapper.readValue(json, clazz);
    }

    /**
     * 获取objectMapper
     *
     * @return {@link ObjectMapper}
     * @date 2022/5/31 17:33
     * @author liming
     */
    public static ObjectMapper getJacksonObjectMapper() {
        return jacksonObjectMapper;
    }
}
