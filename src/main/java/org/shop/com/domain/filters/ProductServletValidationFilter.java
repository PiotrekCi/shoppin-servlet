package org.shop.com.domain.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.shop.com.model.ProductCategory;
import org.shop.com.model.ProductRequest;
import org.shop.com.util.ConverterFactory;
import org.shop.com.util.ErrorMessages;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Arrays;

public class ProductServletValidationFilter implements Filter {
    private final static String PRODUCT_FIELD_ERROR = "Invalid product ";
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        if (httpServletRequest.getMethod().equals(HttpMethod.POST.name())) {
            ProductRequest productRequest = ConverterFactory.getObjectFromServletRequestBody(httpServletRequest, ProductRequest.class);
            final String productErrorMessage = this.validProduct(productRequest);
            if (productErrorMessage != null) {
                httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                httpServletResponse.getWriter().write(
                        ErrorMessages.createErrorMessage(productErrorMessage, HttpStatus.BAD_REQUEST)
                );
                return;
            }
        }
        chain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private String validProduct(ProductRequest productRequest) {
        if (productRequest.getAmount() < 0) {
            return PRODUCT_FIELD_ERROR + "amount";
        }

        if (productRequest.getPrice().doubleValue() <= 0) {
            return PRODUCT_FIELD_ERROR + "price";
        }

        if (!Arrays.stream(ProductCategory.values()).map(Enum::name).toList().contains(productRequest.getProductCategory())) {
            return PRODUCT_FIELD_ERROR + "category";
        }

        if (productRequest.getName().isEmpty()) {
            return PRODUCT_FIELD_ERROR + "name";
        }

        return null;
    }
}
