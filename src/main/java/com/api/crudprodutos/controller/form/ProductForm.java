package com.api.crudprodutos.controller.form;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.api.crudprodutos.model.Product;
import com.api.crudprodutos.repository.ProductRepository;

public class ProductForm {

	@NotNull
	@NotEmpty
	private String description;

	@NotNull
	@NotEmpty
	private String name;

	@NotNull
	@DecimalMin(value = "0.0")
	private BigDecimal price;

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Product converter() {
		return new Product(description, name, price);
	}

	public Product atualizar(ProductRepository repository, Long id) {
		Product product = repository.getReferenceById(id);
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		return product;
	}

}
