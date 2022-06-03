package com.api.crudprodutos.config.handler;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponses {

	private int status_code;
	private List<String> messages;

}
