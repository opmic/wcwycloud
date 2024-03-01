package com.wcwy.common.web.config;

/**
 * ClassName: AutoIdempotentInterceptor
 * Description:
 * date: 2023/7/3 9:28
 *
 * @author tangzhuo
 * @since JDK 1.8
 */


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.wcwy.common.base.exception.ServiceException;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.util.RedisService;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.web.annotation.AutoIdempotent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/** * 拦截器 */
@Component
public class AutoIdempotentInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;
    /** * 预处理 * * @param request * @param response * @param handler * @return * @throws Exception */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //被ApiIdempotment标记的扫描
        AutoIdempotent methodAnnotation = method.getAnnotation(AutoIdempotent.class);
        if (methodAnnotation != null) {
            try {
                return checkToken(request);// 幂等性校验, 校验通过则放行, 校验失败则抛出异常, 并通过统一异常处理返回友好提示
            }catch (Exception ex){
              /*  ResultVo failedResult = ResultVo.getFailedResult(101, ex.getMessage());*/
                R r =new R();
                r.setCode(101);
                r.setMessage(ex.getMessage());
                writeReturnJson(response, JSONUtil.toJsonStr(r));
                throw ex;
            }
        }
        //必须返回true,否则会被拦截一切请求
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String token = request.getHeader("Authorization");
        if(!StringUtils.isEmpty(token)){
            redisService.remove(token);
        }

    }

    /** * 返回的json值 * @param response * @param json * @throws Exception */
    private void writeReturnJson(HttpServletResponse response, String json) throws Exception{
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);
        } catch (IOException e) {
        } finally {
            if (writer != null){
                writer.close();
            }


        }
    }
    public boolean checkToken(HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token)) {// header中不存在token
            token = request.getHeader("tenant_id");
            if (StrUtil.isBlank(token)) {// parameter中也不存在token
                throw new ServiceException("请携带身份信息", 100);
            }
        }
        if (redisService.exists(token)) {
            throw new ServiceException("重复请求", 200);
        }
        redisService.setEx(token,request.getRequestURI(),2L);

      /*  boolean remove = redisService.remove(token);
        if (!remove) {
            throw new ServiceException("ccc", 200);
        }*/
        return true;
    }
}