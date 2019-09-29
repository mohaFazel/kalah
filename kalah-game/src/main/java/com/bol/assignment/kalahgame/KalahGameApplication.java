package com.bol.assignment.kalahgame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class KalahGameApplication {

	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(KalahGameApplication.class, args);
	}

	/**
	 * Identifies the configuration of Swagger in order to have a testable user interface of endpoints.
	 */
	@Bean
	public Docket api(){
		String host = env.getProperty("swagger.host");
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.bol.assignment.kalahgame.resource"))
				.paths(PathSelectors.any())
				.build()
				.host(host)
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Kalah Game")
				.description("An application that simulates 6 stone kalah game as part of the hiring process assignment for Bol.com")
				.build();
	}

}
