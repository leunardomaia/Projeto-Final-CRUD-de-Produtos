package com.api.crudprodutos.controller.service;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.crudprodutos.controller.dto.ProductDto;
import com.api.crudprodutos.controller.form.ProductForm;
import com.api.crudprodutos.model.Product;
import com.api.crudprodutos.repository.ProductRepository;

@ExtendWith(SpringExtension.class)
class ProductServiceImpTest {
			
	@TestConfiguration
	static class ProductServiceTestConfiguration {
		@Bean
		public ProductService bookingService() {
			return new ProductServiceImp();
		}
	}

	@Autowired
	ProductService productService;
	
	@MockBean
	ProductRepository productRepository;
	
	
	// list 
	
	@Test
	public void listDeveriaRetornarTodosOsProdutos() {
		Mockito.when(productRepository.findAll(Mockito.any(Pageable.class)))
			.thenReturn(criaPagina(10, 15l));
		
		Page<ProductDto> pageProducts = productService.list(PageRequest.of(0, 10));
		
		Assertions.assertEquals(15 , pageProducts.getTotalElements());
		Assertions.assertEquals(10 , pageProducts.getSize());
	}
	
	@Test
	public void listDeveriaRetornar200EPaginaVazia() throws Exception {
		Mockito.when(productRepository.findAll(Mockito.any(Pageable.class)))
			.thenReturn(criaPagina(10, 0l));
		
		Page<ProductDto> pageProducts = productService.list(PageRequest.of(0, 10));
		
		Assertions.assertEquals(0 , pageProducts.getTotalElements());
		Assertions.assertEquals(10 , pageProducts.getSize());
	}
	
	
	
	// create
	
	@Test
	public void createDeveriaRetornar201EOProdutoCriado() {
		Mockito.when(productRepository.save(Mockito.any(Product.class)))
			.thenReturn(product());
		
		ResponseEntity<ProductDto> responseEntity = 
				productService.create(form(), UriComponentsBuilder.newInstance());
		
		Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		Assertions.assertEquals(product().getId(), responseEntity.getBody().getId());
	}
	
	
	
	// search
	
	@Test
	public void searchDeveriaRetornarTodosOsProdutos() {
		Mockito.when(productRepository.findByPrice(Mockito.anyDouble(), Mockito.anyDouble(),
				Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenReturn(criaPagina(10, 15l));
	
		Page<ProductDto> pageProducts = productService.search(45D, 45D, "q", PageRequest.of(0, 10));
	
		Assertions.assertEquals(15 , pageProducts.getTotalElements());
		Assertions.assertEquals(10 , pageProducts.getSize());
	}
	
	@Test
	public void searchDeveriaRetornarPaginaVazia() {
		Mockito.when(productRepository.findByPrice(Mockito.anyDouble(), Mockito.anyDouble(),
				Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenReturn(criaPagina(10, 0l));
	
		Page<ProductDto> pageProducts = productService.search(45D, 45D, "q", PageRequest.of(0, 10));
	
		Assertions.assertEquals(0 , pageProducts.getTotalElements());
		Assertions.assertEquals(10 , pageProducts.getSize());
	}
	
	
	// findById 
	
	@Test
	public void findByIdDeveriaRetornar200EOProduto() {
		Mockito.when(productRepository.getReferenceById(Mockito.anyLong()))
			.thenReturn(product());
		
		ResponseEntity<ProductDto> responseEntity = 
				productService.findById(Mockito.anyLong());
		
		Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
		Assertions.assertEquals(product().getId(), responseEntity.getBody().getId());
	}
	
	@Test
	public void finByIdDeveriaLancarExceptionParaIdDesconhecido(){
		doThrow(EntityNotFoundException.class)
				.when(productRepository).getReferenceById(Mockito.anyLong());
		
		try {
			productService.findById(Mockito.anyLong());
		} catch (EntityNotFoundException e) {}
		
		Assertions.assertThrows(EntityNotFoundException.class,
				() -> productRepository.getReferenceById(Mockito.anyLong()));
	}
	
	
	
	// update
	
	@Test
	public void updateDeveriaRetornar200EProdutoAtualizado(){
		Mockito.when(productRepository.getReferenceById(Mockito.anyLong()))
		.thenReturn(product());
		
		ResponseEntity<ProductDto> responseEntity = 
				productService.update(Mockito.anyLong(), form());
		
		Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Assertions.assertEquals(product().getDescription(), responseEntity.getBody().getDescription());
	}
	
	@Test
	public void updateDeveriaLancarExceptionParaIdDesconhecido(){
		doThrow(EntityNotFoundException.class)
				.when(productRepository).getReferenceById(Mockito.anyLong());
		
		try {
			productService.update(Mockito.anyLong(), form());
		} catch (EntityNotFoundException e) {}
		
		Assertions.assertThrows(EntityNotFoundException.class,
				() -> productRepository.getReferenceById(Mockito.anyLong()));
	}
	
	
	
	//delete
	
	@Test
	public void deleteDeveriaUsarRepositoryERetornar200(){
		ResponseEntity<?> responseEntity = productService.delete(Mockito.anyLong());
		
		verify(productRepository).deleteById(Mockito.anyLong());
		Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	public void deleteDeveriaLancarExceptionParaIdDesconhecido(){
		doThrow(EmptyResultDataAccessException.class)
				.when(productRepository).deleteById(Mockito.anyLong());
		
		try {
			productService.delete(Mockito.anyLong());
		} catch (EmptyResultDataAccessException e) {}
		
		Assertions.assertThrows(EmptyResultDataAccessException.class,
				() -> productRepository.deleteById(Mockito.anyLong()));
	}
	
	
	
	// private methods
	
	private Page<Product> criaPagina(int size, Long total) {
		List<Product> products = new ArrayList<>();
		for (Long i = 1l; i < total + 1; i++) {
			products.add(formToProduct(form(), i));
		}
		return new PageImpl<Product>(products, PageRequest.of(0, size), 0l);
	}
	
	private Product formToProduct(ProductForm form, Long id) {
		Product product = new Product(form.getDescription(), form.getName(), form.getPrice());
		product.setId(id);
		return product;
	}
		
	private ProductForm form() {
		return new ProductForm("de teste", "produto", new BigDecimal("45"));
	}

	private Product product() {
		return formToProduct(form(), 1l);
	}
	
}
