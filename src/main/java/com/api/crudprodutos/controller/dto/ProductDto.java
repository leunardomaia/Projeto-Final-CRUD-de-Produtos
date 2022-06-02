package com.api.crudprodutos.controller.dto;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;

import com.api.crudprodutos.model.Product;

public class ProductDto {
	
	private String description;
	private Long id;
	private String name;
	private BigDecimal price;

	public ProductDto(Product product) {
		this.description = product.getDescription();
		this.id = product.getId();
		this.name = product.getName();
		this.price = product.getPrice();
	}

	public String getDescription() {
		return description;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}
	
	public static Page<ProductDto> converter(Page<Product> products) {
		return products.map(ProductDto::new);
	}
	
}
