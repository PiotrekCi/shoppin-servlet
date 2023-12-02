package org.shop.com.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.shop.com.model.*;
import org.shop.com.repository.mongo.ProductChangelogRepository;
import org.shop.com.repository.mongo.ProductRepository;
import org.shop.com.util.ConverterFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductChangelogRepository productChangelogRepository;

    @Transactional
    public String createProduct(HttpServletRequest request) throws IOException {
        ProductRequest productToSave = ConverterFactory.getObjectFromServletRequestBody(request, ProductRequest.class);

        Product newProduct = productRepository.save(
                Product.builder()
                        .productCategory(ProductCategory.valueOf(productToSave.getProductCategory()))
                        .image(productToSave.getImage())
                        .amount(productToSave.getAmount())
                        .description(productToSave.getDescription())
                        .name(productToSave.getName())
                        .price(productToSave.getPrice())
                        .build()
        );

        ProductChangelog productChangelog = new ProductChangelog();
        productChangelog.setProductId(newProduct.getId());
        productChangelogRepository.save(productChangelog);

        return ConverterFactory.getStringFromObject(newProduct);
    }

    public String getProducts() throws JsonProcessingException {
        return ConverterFactory.getStringFromObjectIgnoringFields(
                productRepository.findAllBase(),
                "createdAt", "modifiedAt", "description", "available", "amount"
        );
    }

    public String getProductDetails(HttpServletRequest httpServletRequest) throws JsonProcessingException {
        String productId = ConverterFactory.getPathVariable(httpServletRequest, 1);
        Product product = productRepository.findById(productId).orElseThrow();
        return ConverterFactory.getStringFromObject(product);
    }

    @Transactional
    public String deleteProduct(HttpServletRequest request) {
        String productId = ConverterFactory.getPathVariable(request, 1);
        productRepository.deleteById(productId);
        return productId;
    }

    @Transactional
    public String updateProduct(HttpServletRequest httpServletRequest) throws IOException {
        ProductRequest productRequest = ConverterFactory.getObjectFromServletRequestBody(httpServletRequest, ProductRequest.class);
        String productId = ConverterFactory.getPathVariable(httpServletRequest, 1);
        Product currentProduct = productRepository.findById(productId).orElseThrow();
        ProductChangelog productChangelog = productChangelogRepository.findByProductId(productId).orElseThrow();
        productChangelog.addChangeLog(currentProduct);
        this.updateProductAttributes(currentProduct, productRequest);
        Product updatedProduct = productRepository.save(currentProduct);
        productChangelogRepository.save(productChangelog);
        return ConverterFactory.getStringFromObject(updatedProduct);
    }

    private void updateProductAttributes(Product productToUpdate, ProductRequest updateRequest) {
        productToUpdate.setImage(updateRequest.getImage());
        productToUpdate.setPrice(updateRequest.getPrice());
        productToUpdate.setProductCategory(ProductCategory.valueOf(updateRequest.getProductCategory()));
        productToUpdate.setName(updateRequest.getName());
        productToUpdate.setAmount(updateRequest.getAmount());
        productToUpdate.setDescription(updateRequest.getDescription());
    }
}
