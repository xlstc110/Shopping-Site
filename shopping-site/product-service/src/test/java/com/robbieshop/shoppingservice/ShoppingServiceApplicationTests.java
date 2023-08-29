package com.robbieshop.shoppingservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robbieshop.shoppingservice.dto.ProductRequest;
import com.robbieshop.shoppingservice.repository.ProductRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers//JUnit 5 now knows we are using test containers to run this test
@AutoConfigureMockMvc//To configure MockMvc as a dependency
class ShoppingServiceApplicationTests {

	@Container//Junit 5 now knows this is a container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer();
	@Autowired//Dependent injection for MockMVC
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;//This object can convert POJO object to JSON, or JSON object to POJO
	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestJson = objectMapper.writeValueAsString(productRequest);

		//The MockMvcRequestBuilders can mock a client request in different request type, get, post, update, delete
		//1. The mock will make a call to the API /api/product with a Post request type, and expect the status is Created.
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestJson))
				.andExpect(status().isCreated());
		//sample respond: Prodcut 64ed82d94307cf3f1673326a is saved

		//Validate again that the database successfully added the product:
		Assertions.assertTrue(productRepository.findAll().size() > 0);
	}

	@Test
	void shouldGetAllProducts() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk());
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("Iphone 15")
				.description("Iphone 15, 2023")
				.price(BigDecimal.valueOf(1399))
				.build();
	}

}
