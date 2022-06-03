package com.api.crudprodutos.config.swagger;

import org.springframework.lang.Nullable;

import io.swagger.annotations.ApiParam;
import lombok.Getter;

@Getter
class SwaggerPageable {

	@ApiParam(value = "Pagina a ser carregada", example = "0")
	@Nullable
	private Integer page;

	@ApiParam(value = "Quantidade de produtos", example = "5")
	@Nullable
	private Integer size;

	@ApiParam(value = "Ordenacao dos produtos")
	@Nullable
	private String sort;

}
