package org.shop.com.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.shop.com.model.*;
import org.shop.com.repository.mongo.OrderRepository;
import org.shop.com.repository.mongo.ProductRepository;
import org.shop.com.util.ConverterFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    public String createOrder(HttpServletRequest httpServletRequest) throws IOException {
        CustomerOrderRequest customerOrderRequest = ConverterFactory.getObjectFromServletRequest(httpServletRequest, CustomerOrderRequest.class);
        List<Product> productList = productRepository.findAllById(customerOrderRequest.getProducts().stream().map(String::valueOf).collect(Collectors.toList()));

        CustomerOrder savedOrder = null;
        productRepository.saveAll(productList);
        return ConverterFactory.getStringFromObject(savedOrder);
    }

    public String getOrderDetails(HttpServletRequest httpServletRequest) throws JsonProcessingException {
        Integer orderId = Integer.valueOf(ConverterFactory.getPathVariable(httpServletRequest, 1));
        CustomerOrder order = orderRepository.findById(orderId.toString()).orElseThrow();
        return ConverterFactory.getStringFromObject(order);
    }

    public CustomerOrder getOrderForAction(HttpServletRequest httpServletRequest) {
        Integer orderId = Integer.valueOf(ConverterFactory.getPathVariable(httpServletRequest, 1));
        return orderRepository.findById(orderId.toString()).orElseThrow();
    }
}
