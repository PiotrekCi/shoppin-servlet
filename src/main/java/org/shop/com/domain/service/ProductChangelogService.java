package org.shop.com.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.shop.com.model.ProductChangelog;
import org.shop.com.repository.mongo.ProductChangelogRepository;
import org.shop.com.util.ConverterFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductChangelogService {
    private final ProductChangelogRepository productChangelogRepository;

    public String getProductChangelog(HttpServletRequest httpServletRequest) throws JsonProcessingException {
        String productId = ConverterFactory.getPathVariable(httpServletRequest, 1);
        ProductChangelog productChangelog = this.productChangelogRepository.findByProductId(productId).orElseThrow();
        return ConverterFactory.getStringFromObject(productChangelog);
    }
}
