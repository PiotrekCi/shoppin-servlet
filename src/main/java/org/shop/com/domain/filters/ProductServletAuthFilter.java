package org.shop.com.domain.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.shop.com.util.ErrorMessages;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
public class ProductServletAuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        if (!httpServletRequest.getRequestURI().equals("/products") && !httpServletRequest.getRequestURI().matches("/products/\\d+")) {
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.getWriter().write(
                    ErrorMessages.createErrorMessage(
                            ErrorMessages.FORBIDDEN,
                            HttpStatus.UNAUTHORIZED
                    )
            );
            return;
        }

        if ((HttpMethod.POST.name()).equalsIgnoreCase(httpServletRequest.getMethod()) ||
                (HttpMethod.DELETE.name()).equalsIgnoreCase(httpServletRequest.getMethod()) ||
                (HttpMethod.PATCH.name()).equalsIgnoreCase(httpServletRequest.getMethod()))
        {
            String authHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer ") && authHeader.substring(7).equals("abcdefgh12345")) {
                chain.doFilter(httpServletRequest, httpServletResponse);
            } else {
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                httpServletResponse.getWriter().write(
                        ErrorMessages.createErrorMessage(
                                ErrorMessages.INVALID_OR_MISSING_AUTHORIZATION_HEADER,
                                HttpStatus.UNAUTHORIZED
                        )
                );
            }
            return;
        }
        chain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
