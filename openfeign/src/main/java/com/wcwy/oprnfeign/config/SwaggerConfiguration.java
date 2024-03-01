/*
package com.wcwy.oprnfeign.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

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
                .apis(RequestHandlerSelectors.basePackage("com.wcwy.oprnfeign"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("岗位管理")
                .description("<div style='font-size:14px;color:red;'>岗位管理</div>")
                .termsOfServiceUrl("https://www.danir.store")
                .contact(new Contact("yl", "https://github.com/dnir", "994242104@qq.com"))
                .license("Open Source")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .version("1.0.0")
                .build();
    }

}





*/
