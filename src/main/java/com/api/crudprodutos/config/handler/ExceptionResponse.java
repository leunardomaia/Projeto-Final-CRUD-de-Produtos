package com.api.crudprodutos.config.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponse {

	private int status_code;
	private String message;

}
