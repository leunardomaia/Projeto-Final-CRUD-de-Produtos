package com.api.crudprodutos.controller;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.crudprodutos.controller.dto.ProductDto;
import com.api.crudprodutos.controller.form.ProductForm;
import com.api.crudprodutos.controller.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	ProductService productService;

	@GetMapping
	public Page<ProductDto> list(
			@PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable paginacao) {
		return productService.list(paginacao);
	}

	@Transactional
	@PostMapping
	public ResponseEntity<ProductDto> create(@RequestBody @Valid ProductForm form, UriComponentsBuilder uriBuilder) {
		return productService.create(form, uriBuilder);
	}

	@GetMapping("/search")
	public Page<ProductDto> search(@RequestParam(required = false) Double max_price,
			@RequestParam(required = false) Double min_price, @RequestParam(required = false) String q,
			@PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable paginacao) {
		return productService.search(max_price, min_price, q, paginacao);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductDto> findById(@PathVariable Long id) {
		return productService.findById(id);
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody @Valid ProductForm form) {
		return productService.update(id, form);
	}

	@Transactional
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		return productService.delete(id);
	}

}
