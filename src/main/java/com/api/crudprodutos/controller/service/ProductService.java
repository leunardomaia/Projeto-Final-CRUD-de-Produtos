package com.api.crudprodutos.controller.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.crudprodutos.controller.dto.ProductDto;
import com.api.crudprodutos.controller.form.ProductForm;

public interface ProductService {

	Page<ProductDto> list(Pageable paginacao);

	ResponseEntity<ProductDto> create(ProductForm form, UriComponentsBuilder uriBuilder);

	Page<ProductDto> search(Double max_price, Double min_price, String q, Pageable paginacao);

	ResponseEntity<ProductDto> findById(Long id);

	ResponseEntity<ProductDto> update(Long id, ProductForm form);

	ResponseEntity<?> delete(Long id);

}
