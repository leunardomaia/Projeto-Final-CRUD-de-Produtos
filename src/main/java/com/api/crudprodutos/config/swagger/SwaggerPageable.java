package com.api.crudprodutos.config.swagger;

import org.springframework.lang.Nullable;

import io.swagger.annotations.ApiParam;

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

	public Integer getPage() {
		return page;
	}

	public Integer getSize() {
		return size;
	}

	public String getSort() {
		return sort;
	}

}
