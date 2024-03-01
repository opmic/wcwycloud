package com.wcwy.company.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

@EnableKnife4j
@Configuration
//@EnableSwagger2WebMvc
public class SwaggerConfiguration {

    @Bean
    public Docket restApi() {
        //scope方位
        List<AuthorizationScope> scopes=new ArrayList<>();
        scopes.add(new AuthorizationScope("read","read  resources"));
        scopes.add(new AuthorizationScope("write","write resources"));
        scopes.add(new AuthorizationScope("reads","read all resources"));
        scopes.add(new AuthorizationScope("writes","write all resources"));
        //schemas
        //securyContext
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis( RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class ) )
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("企业管理+推荐官管理+求职者管理")
                .description("<div style='font-size:14px;color:red;'>企业管理+推荐官管理+求职者管理</div>")
                .termsOfServiceUrl("https://www.danir.store")
                .contact(new Contact("yl", "https://github.com/dnir", "994242104@qq.com"))
                .license("Open Source")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .version("1.0.0")
                .build();
    }

}





