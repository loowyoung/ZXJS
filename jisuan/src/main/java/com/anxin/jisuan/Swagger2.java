package com.anxin.jisuan;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author: ly
 * @date: 2019/3/18 14:50
 *
 * http://localhost:8085/swagger-ui.html
 */
@Configuration
public class Swagger2 {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.anxin.jisuan"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("天地图绘制api文档")
                .description("在天地图上绘制泄漏区域,https://github.com/loowyoung/ZXJS")
                .termsOfServiceUrl("https://github.com/loowyoung/ZXJS")
                .version("1.0")
                .build();
    }

}