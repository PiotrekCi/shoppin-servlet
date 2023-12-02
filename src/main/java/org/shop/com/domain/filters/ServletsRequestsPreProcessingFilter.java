package org.shop.com.domain.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.shop.com.util.ConverterFactory;

import java.io.IOException;

public class ServletsRequestsPreProcessingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest httpServletRequest) {
            String requestBody = ConverterFactory.getRequestBodyFromInputStream(httpServletRequest);
            httpServletRequest.setAttribute("requestBody", requestBody);
            chain.doFilter(httpServletRequest, servletResponse);
            return;
        }
        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
