package org.shop.com.config;

import org.shop.com.domain.filters.ProductServletAuthFilter;
import org.shop.com.domain.filters.ProductServletValidationFilter;
import org.shop.com.domain.filters.ServletsRequestsPreProcessingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterOrderConfig {
    // FILTERS ORDER ARE:
    // 1. AUTHORIZATION FILTERS
    // 2. REQUEST PRE PROCESSING FILTER
    // 3. REQUEST VALIDATION FILTERS
    // 4. OTHER FILTERS

    // AUTH FILTERS ------------------------------------------------------------------------------------------
    @Bean
    public FilterRegistrationBean<ProductServletAuthFilter> productServletAuthFilter() {
        FilterRegistrationBean<ProductServletAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ProductServletAuthFilter());
        registrationBean.addUrlPatterns("/products");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    // REQUESTS PRE-PROCESSING FILTER -----------------------------------------------------------------------
    @Bean
    public FilterRegistrationBean<ServletsRequestsPreProcessingFilter> servletsRequestsPreProcessingFilter() {
        FilterRegistrationBean<ServletsRequestsPreProcessingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ServletsRequestsPreProcessingFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    // VALIDATION FILTERS -----------------------------------------------------------------------------------
    @Bean
    public FilterRegistrationBean<ProductServletValidationFilter> productServletValidationFilter() {
        FilterRegistrationBean<ProductServletValidationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ProductServletValidationFilter());
        registrationBean.addUrlPatterns("/products");
        registrationBean.setOrder(3);
        return registrationBean;
    }
}
