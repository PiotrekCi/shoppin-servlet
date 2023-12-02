package org.shop.com.shoppin.bdd.stepdefs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InvalidPriceAddProductServletSteps {
    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders httpHeaders;
    private HttpEntity<String> entity;

    private String productRequest;
    private final String AUTH_HEADER = "Bearer abcdefgh12345";
    private final String servletUrl = "/products";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map response;

    @Given("the incorrect price product request:")
    public void givenTheFollowingProductRequest(DataTable productRequest) throws JsonProcessingException {
        this.productRequest = objectMapper.writeValueAsString(productRequest.asMap());
    }

    @When("user tries to create product")
    public void whenUserTriesToCreateProduct() {
        this.httpHeaders = new HttpHeaders();
        this.httpHeaders.set(HttpHeaders.AUTHORIZATION, AUTH_HEADER);
        this.entity = new HttpEntity<>(this.productRequest, this.httpHeaders);
        this.response = restTemplate.postForObject(
                this.servletUrl,
                this.entity,
                Map.class
        );
        assertNotNull(this.response);
    }

    @Then("api should respond with bad request")
    public void apiShouldRespondWithBadRequest() {
        assertEquals("400 BAD_REQUEST", this.response.get("status"));
    }

    @Then("the response should contain error message")
    public void responseShouldContainErrorMessage() {
        assertEquals("Invalid product price", this.response.get("error"));
    }
}
