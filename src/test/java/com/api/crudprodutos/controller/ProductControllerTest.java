package com.api.crudprodutos.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.crudprodutos.controller.dto.ProductDto;
import com.api.crudprodutos.controller.form.ProductForm;
import com.api.crudprodutos.controller.service.ProductService;
import com.api.crudprodutos.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	ProductService productService;
	
	@Autowired
	ObjectMapper objectMapper;
	
	
	// list
	
	@Test
	public void listDeveriaRetornar200ETodosOsProdutos() throws Exception {
		Mockito.when(productService.list(Mockito.any(Pageable.class)))
			.thenReturn(createPage(10, 15l));
		
		mockMvc.perform(get("/products"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.totalElements").value(15))
			.andExpect(jsonPath("$.size").value(10));
	}

	@Test
	public void listDeveriaRetornar200EPaginaVazia() throws Exception {
		Mockito.when(productService.list(Mockito.any(Pageable.class)))
			.thenReturn(createPage(10, 0l));
		
		mockMvc.perform(get("/products"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.totalElements").value(0))
			.andExpect(jsonPath("$.size").value(10));
	}
	
	

	// create
	
	@Test
	public void postDeveriaRetornar201() throws Exception {
		Mockito.when(productService.create(Mockito.any(ProductForm.class), Mockito.any(UriComponentsBuilder.class)))
			.thenReturn(new ResponseEntity<>(dto(), HttpStatus.CREATED));
		
		mockMvc.perform(post("/products")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(form())))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(dto().getId()));
		}

	@Test
	public void postDeveriaRetornar400ComCampoVazio() throws Exception {
		mockMvc.perform(post("/products")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(formEmpty())))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status_code").value(400));
	}
	
	@Test
	public void postDeveriaRetornar400ComCampoNull() throws Exception {		
		mockMvc.perform(post("/products")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(formNull())))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status_code").value(400));
	}
	
	@Test
	public void postDeveriaRetornar400SemCorpo() throws Exception {
		mockMvc.perform(post("/products"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status_code").value(400));
	}
	
	
	
	// search
	
	@Test
	public void searchDeveriaRetornarTodosOsProdutos() throws Exception {
		Mockito.when(productService.search(Mockito.any(Double.class), Mockito.any(Double.class), 
				Mockito.any(String.class), Mockito.any(Pageable.class)))
			.thenReturn(createPage(10, 15l));

		mockMvc.perform(get("/products/search")
					.queryParam("max_price", "45")
					.queryParam("min_price", "45")
					.queryParam("q", "qualquer coisa"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements").value(15))
				.andExpect(jsonPath("$.size").value(10));
	}
	
	@Test
	public void searchDeveriaRetornarPaginaVazia() throws Exception {
		Mockito.when(productService.search(Mockito.any(Double.class), Mockito.any(Double.class), 
				Mockito.any(String.class), Mockito.any(Pageable.class)))
			.thenReturn(createPage(10, 0l));

		mockMvc.perform(get("/products/search")
				.queryParam("max_price", "45")
				.queryParam("min_price", "45")
				.queryParam("q", "qualquer coisa"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.totalElements").value(0))
			.andExpect(jsonPath("$.size").value(10));
	}
	
	
	
	// findById
	
	@Test
	public void findByIdDeveriaRetornar200EOProduto() throws Exception {
		Mockito.when(productService.findById(Mockito.anyLong()))
			.thenReturn(new ResponseEntity<>(dto(), HttpStatus.OK));
		
		mockMvc.perform(get("/products/{id}", "123"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(dto().getId()));
	}
	
	@Test
	public void findByIdDeveriaRetornar404ParaIdDesconhecido() throws Exception {
		Mockito.when(productService.findById(Mockito.anyLong()))
			.thenThrow(EntityNotFoundException.class);
		
		mockMvc.perform(get("/products/{id}", "123"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.status_code").value(404));
	}
	
	
	
	// update 
	
	@Test
	public void updateDeveriaRetornar200EProdutoAtualizado() throws Exception {
		Mockito.when(productService.update(Mockito.anyLong(), Mockito.any(ProductForm.class)))
		.thenReturn(new ResponseEntity<>(dto(), HttpStatus.OK));
		
		mockMvc.perform(put("/products/{id}", "123")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(form())))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.description").value(dto().getDescription()));
	}
	
	@Test
	public void updateDeveriaRetornar404ParaIdDesconhecido() throws Exception {
		Mockito.when(productService.update(Mockito.anyLong(), Mockito.any(ProductForm.class)))
		.thenThrow(EntityNotFoundException.class);
		
		mockMvc.perform(put("/products/{id}", "123")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(form())))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.status_code").value(404));
	}
	
	@Test
	public void updateDeveriaRetornar400SemCorpo() throws Exception {
		mockMvc.perform(put("/products/{id}", "123"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status_code").value(400));
	}
	
	@Test
	public void updateDeveriaRetornar400ComCampoVazio() throws Exception {
		mockMvc.perform(put("/products/{id}", "123")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(formEmpty())))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status_code").value(400));
	}
	
	@Test
	public void updateDeveriaRetornar400ComCampoNull() throws Exception {		
		mockMvc.perform(put("/products/{id}", "123")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(formNull())))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status_code").value(400));
	}
	
	
	
	// delete
	
	@Test
	public void deleteDeveriaRetornar200() throws Exception {
		
		Mockito.when(productService.delete(Mockito.anyLong()))
			.thenReturn(new ResponseEntity<>(HttpStatus.OK));
		
		mockMvc.perform(delete("/products/{id}", "123"))
			.andExpect(status().isOk());
	}
	
	@Test
	public void deleteDeveriaRetornar404ParaIdDesconhecido() throws Exception {
		
		Mockito.when(productService.delete(Mockito.anyLong()))
			.thenThrow(EmptyResultDataAccessException.class);
		
		mockMvc.perform(delete("/products/{id}", "123"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.status_code").value(404));
	}
	
	
	
	// private methods
	
	private Page<ProductDto> createPage(int size, Long total) {
		List<ProductDto> dtos = new ArrayList<ProductDto>();
		for (Long i = 1l; i < total + 1; i++) {
			dtos.add(formToDto(form(), i));
		}
		return new PageImpl<ProductDto>(dtos, PageRequest.of(0, size), 0l);
	}

	private ProductDto formToDto(ProductForm form, Long id) {
		Product product = new Product(form.getDescription(), form.getName(), form.getPrice());
		product.setId(id);
		return new ProductDto(product);
	}
	
	private ProductForm form() {
		return new ProductForm("de teste", "produto", new BigDecimal("45"));	
	}
	
	private ProductDto dto() {
		return formToDto(form(), 1L);
	}
	
	private ProductForm formEmpty() {
		return new ProductForm("", form().getName(), form().getPrice());
	}
	
	private ProductForm formNull() {
		return new ProductForm(form().getDescription(), form().getName(), null);
	}
}
