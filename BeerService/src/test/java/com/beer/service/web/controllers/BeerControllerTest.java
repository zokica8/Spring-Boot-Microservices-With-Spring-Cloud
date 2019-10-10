package com.beer.service.web.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.beer.service.web.dto.BeerDto;
import com.beer.service.web.dto.BeerStyle;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BeerController.class)
class BeerControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	BeerDto getValidBeer() {
		return BeerDto.builder()
				.beerName("Valjevsko")
				.beerStyle(BeerStyle.LAGER)
				.price(new BigDecimal("90.0"))
				.upc(189232L)
				.build();
	}
	
	@Test
	void testGetBeerById() throws Exception {
		mockMvc.perform(get("/api/v1/beer/" + UUID.randomUUID().toString())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	void testCreateBeer() throws Exception {
		BeerDto beer = getValidBeer();
		String beerJSON = objectMapper.writeValueAsString(beer);
		
		mockMvc.perform(post("/api/v1/beer")
				.contentType(MediaType.APPLICATION_JSON)
				.content(beerJSON))
				.andExpect(status().isCreated());
			
	}

	@Test
	void testUpdateBeerById() throws Exception {
		BeerDto beer = getValidBeer();
		
		String beerJSON = objectMapper.writeValueAsString(beer);
		
		mockMvc.perform(put("/api/v1/beer/" + UUID.randomUUID().toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(beerJSON))
				.andExpect(status().isNoContent());
	}

}
