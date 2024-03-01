package com.wcwy.common.web.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author： 乐哥聊编程(全平台同号)
 */
@Component
public class RequestOriginParserDefinition implements RequestOriginParser {
    // 获取调用方标识信息并返回
    @Override
    public String parseOrigin(HttpServletRequest request) {
        String serviceName = request.getHeader("serviceName");
        return serviceName;
    }
}