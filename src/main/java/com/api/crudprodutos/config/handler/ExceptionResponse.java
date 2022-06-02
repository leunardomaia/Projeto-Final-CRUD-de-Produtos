package com.api.crudprodutos.config.handler;

public class ExceptionResponse {

	private int status_code;
	private String message;
	
	public ExceptionResponse(int status_code, String mensagem) {
		this.status_code = status_code;
		this.message = mensagem;	}

	public int getStatus_code() {
		return status_code;
	}

	public String getMessage() {
		return message;
	}

}
