package com.wcwy.gateway.filter;





import lombok.Data;

import java.lang.annotation.*;

/**
 * 限流注解
 * 
 * @author ruoyi
 */
@Data
public class RateLimiter
{
    /**
     * 限流key
     */
    public static String key="rate_limit:";

    /**
     * 限流时间,单位秒
     */
    public static int time= 60;

    /**
     * 限流次数
     */
    public static int count= 1;


}
