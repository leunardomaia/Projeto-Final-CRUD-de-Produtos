package com.api.crudprodutos.controller.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.crudprodutos.controller.dto.ProductDto;
import com.api.crudprodutos.controller.form.ProductForm;
import com.api.crudprodutos.model.Product;
import com.api.crudprodutos.repository.ProductRepository;

@Service
public class ProductServiceImp implements ProductService {

	@Autowired
	private ProductRepository repository;

	@Override
	public Page<ProductDto> list(Pageable paginacao) {
		Page<Product> products = repository.findAll(paginacao);
		return ProductDto.converter(products);
	}

	@Override
	public ResponseEntity<ProductDto> create(ProductForm form, UriComponentsBuilder uriBuilder) {
		Product product = form.converter();
		product = repository.save(product);
		URI uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
		return ResponseEntity.created(uri).body(new ProductDto(product));
	}

	@Override
	public Page<ProductDto> search(Double max_price, Double min_price, String q, Pageable paginacao) {
		Page<Product> products = repository.findByPrice(max_price, min_price, q, paginacao);
		return ProductDto.converter(products);
	}

	@Override
	public ResponseEntity<ProductDto> findById(Long id) {
		Product product = repository.getReferenceById(id);
		return ResponseEntity.ok(new ProductDto(product));
	}

	@Override
	public ResponseEntity<ProductDto> update(Long id, ProductForm form) {
		Product product = form.atualizar(repository, id);
		return ResponseEntity.ok(new ProductDto(product));
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		repository.deleteById(id);
		return ResponseEntity.ok().build();
	}

}
