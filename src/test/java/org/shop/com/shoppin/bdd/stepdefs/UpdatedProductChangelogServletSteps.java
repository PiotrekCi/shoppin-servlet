package org.shop.com.shoppin.bdd.stepdefs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.shop.com.model.Product;
import org.shop.com.model.ProductChangelog;
import org.shop.com.repository.mongo.ProductRepository;
import org.shop.com.util.ConverterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UpdatedProductChangelogServletSteps {
    @Autowired
    RestTemplate restTemplate;
    private String existingProductId;
    @Autowired
    private ProductRepository productRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private HttpHeaders httpHeaders;
    private HttpEntity<String> entity;
    private String productRequest;
    private Product updateResponse;
    private ProductChangelog changelogResponse;
    private final String AUTH_HEADER = "Bearer abcdefgh12345";
    private final String productServletUrl = "http://localhost:53868/products";
    private final String productChangelogServletUrl = "http://localhost:53868/products-changelog";

    @Given("the following existing product data:")
    public void givenTheFollowingExistingProductData(DataTable existingProduct) throws JsonProcessingException {
        String request =  ConverterFactory.getStringFromObject(existingProduct.asMap());
        this.httpHeaders = new HttpHeaders();
        this.httpHeaders.set(HttpHeaders.AUTHORIZATION, AUTH_HEADER);
        this.entity = new HttpEntity<>(request, this.httpHeaders);
        this.existingProductId = restTemplate.postForObject(this.productServletUrl, this.entity, Product.class).getId();
    }

    @When("the user updates the product with the following changes:")
    public void userUpdatesTheProductWithFollowingChanges(DataTable updateRequest) throws JsonProcessingException {
        this.productRequest = ConverterFactory.getStringFromObject(updateRequest.asMap());
        this.entity = new HttpEntity<>(this.productRequest, this.httpHeaders);
        this.updateResponse = restTemplate.patchForObject(
                this.productServletUrl + "/" + this.existingProductId,
                this.entity,
                Product.class
        );
        assertNotNull(this.updateResponse);
    }

    @And("the user requests for updated product changelog")
    public void userRequestsForUpdatedProductChangelog() throws JsonProcessingException {
        this.changelogResponse = ConverterFactory.getObjectFromString(
                restTemplate.getForObject(
                this.productChangelogServletUrl + "/" + this.existingProductId,
                String.class
            ),
            ProductChangelog.class
        );
    }

    @Then("the response should contain the data before the update:")
    public void shouldContainTheDataBeforeUpdate(DataTable data) throws JsonProcessingException {
        String changelogData = ConverterFactory.getStringFromObject(data.asMap());
        Product productBeforeUpdate = ConverterFactory.getObjectFromString(changelogData, Product.class);
        LocalDateTime changesDate = this.changelogResponse.getChanges().keySet().stream().toList().get(0);
        Map<String, Object> changes = this.changelogResponse.getChanges().get(changesDate);
        assertEquals(productBeforeUpdate.getPrice().toString(), changes.get("price"));
        assertEquals(productBeforeUpdate.getAmount(), changes.get("amount"));
        assertEquals(productBeforeUpdate.getName(), changes.get("name"));
        assertEquals(productBeforeUpdate.getDescription(), changes.get("description"));
        assertEquals(productBeforeUpdate.getImage(), changes.get("image"));
    }
}
