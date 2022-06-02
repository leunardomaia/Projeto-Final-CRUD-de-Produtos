package com.api.crudprodutos.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;

import com.api.crudprodutos.model.Product;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfigurations {
	
	@Bean
	public Docket crudApi( ) {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.api.crudprodutos"))
				.paths(PathSelectors.ant("/**"))
				.build()
				.ignoredParameterTypes(Product.class)
				.directModelSubstitute(Pageable.class, SwaggerPageable.class);
	}
	
}
