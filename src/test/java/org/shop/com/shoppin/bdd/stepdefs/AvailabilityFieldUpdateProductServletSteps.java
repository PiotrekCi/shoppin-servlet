package org.shop.com.shoppin.bdd.stepdefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.shop.com.model.Product;
import org.shop.com.model.ProductCategory;
import org.shop.com.model.ProductChangelog;
import org.shop.com.repository.mongo.ProductChangelogRepository;
import org.shop.com.repository.mongo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AvailabilityFieldUpdateProductServletSteps {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductChangelogRepository productChangelogRepository;

    private HttpHeaders httpHeaders;
    private HttpEntity<String> entity;

    private String productUpdateRequest;
    private final String AUTH_HEADER = "Bearer abcdefgh12345";
    private String createdProductId;
    private final String servletUrl = "http://localhost:53868/products";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Product updatedProduct;

    @Given("product with amount count 0:")
    public void productRequestWithAmountCount0(DataTable productUpdateRequest) throws IOException {
        this.createdProductId = productRepository.save(
                Product.builder()
                        .productCategory(ProductCategory.EXAMPLE)
                        .amount(10)
                        .image("imageurl")
                        .price(BigDecimal.valueOf(1000))
                        .description("test")
                        .name("Produkt 1")
                        .build()
        ).getId();
        productChangelogRepository.save(
                ProductChangelog.builder()
                        .productId(this.createdProductId)
                        .build()
        );
        this.productUpdateRequest = objectMapper.writeValueAsString(productUpdateRequest.asMap());
    }

    @When("user updates product")
    public void userUpdatesProduct() {
        this.httpHeaders = new HttpHeaders();
        this.httpHeaders.set(HttpHeaders.AUTHORIZATION, AUTH_HEADER);
        this.entity = new HttpEntity<>(this.productUpdateRequest, this.httpHeaders);
        this.updatedProduct = restTemplate.patchForObject(
                this.servletUrl + "/" + this.createdProductId,
                this.entity,
                Product.class
        );
    }

    @Then("api should respond with updated product")
    public void apiRespondWithUpdatedProduct() {
        assertNotNull(this.updatedProduct);
        assertEquals(this.createdProductId, this.updatedProduct.getId());
    }

    @Then("the updated product availability field should be false")
    public void availabilityFieldIsFalse() {
        assertFalse(this.updatedProduct.getAvailable());
    }
}
