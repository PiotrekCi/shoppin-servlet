package org.shop.com.shoppin.bdd.stepdefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.shop.com.model.Product;
import org.shop.com.model.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class SuccessfulAddProductServletSteps {
    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders httpHeaders;
    private HttpEntity<String> entity;

    private String productRequest;
    private final String AUTH_HEADER = "Bearer abcdefgh12345";
    private String createdProductId;
    private final String servletUrl = "/products";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Product createdProduct;

    @Given("the correct product request:")
    public void givenTheFollowingProductRequest(DataTable productRequest) throws IOException {
        this.productRequest = objectMapper.writeValueAsString(productRequest.asMap());
    }

    @When("the user creates the product")
    public void whenTheUserCreatesTheProduct() {
        this.httpHeaders = new HttpHeaders();
        this.httpHeaders.set(HttpHeaders.AUTHORIZATION, AUTH_HEADER);
        this.entity = new HttpEntity<>(this.productRequest, this.httpHeaders);
        this.createdProductId = restTemplate.postForObject(
                this.servletUrl,
                this.entity,
                Product.class
        ).getId();
        assertNotNull(this.createdProductId);
    }

    @Then("the product should be created successfully")
    public void thenTheProductShouldBeCreatedSuccessfully() {
        this.createdProduct = this.restTemplate.getForEntity("/products/"+this.createdProductId, Product.class).getBody();
        assertNotNull(this.createdProduct);
    }

    @Then("the response should contain the product details")
    public void thenTheResponseShouldContainTheProductDetails() {
        assertEquals("Nowy Produkt", this.createdProduct.getName());
        assertEquals("Opis Produktu", this.createdProduct.getDescription());
        assertEquals(ProductCategory.EXAMPLE, this.createdProduct.getProductCategory());
        assertEquals(this.createdProductId, this.createdProduct.getId());
        assertEquals(BigDecimal.valueOf(1000), this.createdProduct.getPrice());
        assertEquals(100, this.createdProduct.getAmount());
        assertEquals("/path/to/image/base64", this.createdProduct.getImage());
    }
}
