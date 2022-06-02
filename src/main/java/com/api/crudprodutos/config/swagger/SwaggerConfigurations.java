package com.api.crudprodutos.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;

import com.api.crudprodutos.model.Product;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfigurations {

	@Bean
	public Docket crudApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.api.crudprodutos"))
				.paths(PathSelectors.ant("/**"))
				.build()
				.ignoredParameterTypes(Product.class)
				.directModelSubstitute(Pageable.class, SwaggerPageable.class)
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Projeto Final: CRUD de Produtos")
				.description("Um exemplo de aplicação Spring Boot REST API")
				.version("1.0")
				.contact(new Contact("Leonardo Ferreira Maia",
						"https://github.com/leunardomaia/Projeto-Final-CRUD-de-Produtos",
						"leonardo.maia.pb@compasso.com.br"))
				.build();
	}

}
