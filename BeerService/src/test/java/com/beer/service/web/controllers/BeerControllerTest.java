package com.beer.service.web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

import com.beer.service.model.Beer;
import com.beer.service.repositories.BeerRepository;
import com.beer.service.web.dto.BeerDto;
import com.beer.service.web.dto.BeerStyle;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriPort = 8074)
@WebMvcTest(BeerController.class)
@ComponentScan(basePackages = "com.beer.service.mappers")
class BeerControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private BeerRepository beerRepository;
	
	// static class for validating beer constraints
	private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
        }
    }
	
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
		
		given(beerRepository.findById(any())).willReturn(Optional.of(Beer.builder().build()));
		
		mockMvc.perform(get("/api/v1/beer/{id}", UUID.randomUUID().toString())
				.param("isCold", "yes")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("v1/get-beer", 
					pathParameters(
							parameterWithName("id").description("UUID of the desired beer to get.")
					), 
					requestParameters(
							parameterWithName("isCold").description("Is the beer cold or not?")
					),
					responseFields(
							fieldWithPath("id").description("Id of beer").type(UUID.class),
							fieldWithPath("version").description("Beer version number").type(Long.class),
							fieldWithPath("createdDate").description("Date created").type("Date Time"),
							fieldWithPath("lastModifiedDate").description("Date beer was last modified").type("Date Time"),
							fieldWithPath("beerName").description("Beer Name").type(String.class),
							fieldWithPath("beerStyle").description("Beer Style").type(BeerStyle.class),
							fieldWithPath("upc").description("UPC number").type(Long.class),
							fieldWithPath("price").description("Beer price").type(BigDecimal.class),
							fieldWithPath("quantityOnHand").description("Beer quantity").type(Integer.class)
					)
					));
	}

	@Test
	void testCreateBeer() throws Exception {
		BeerDto beer = getValidBeer();
		String beerJSON = objectMapper.writeValueAsString(beer);
		
		ConstrainedFields fields = new ConstrainedFields(BeerDto.class);
		
		mockMvc.perform(post("/api/v1/beer")
				.contentType(MediaType.APPLICATION_JSON)
				.content(beerJSON))
				.andExpect(status().isCreated())
				.andDo(document("v1/new-beer", 
						requestFields(
								fields.withPath("id").ignored(),
								fields.withPath("version").ignored(),
								fields.withPath("createdDate").ignored(),
								fields.withPath("lastModifiedDate").ignored(),
								fields.withPath("beerName").description("Beer Name")
										.attributes(key("constraints").value("Name can't be blank! Also, size must be between 3 and 100 characters!")),
								fields.withPath("beerStyle").description("Beer Style").attributes(),
								fields.withPath("upc").description("UPC").attributes(),
								fields.withPath("price").description("Beer Price"),
								fields.withPath("quantityOnHand").ignored()
						)
						));
			
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
