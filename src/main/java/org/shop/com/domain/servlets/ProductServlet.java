package org.shop.com.domain.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
//import org.shop.com.domain.service.ProductService;
//import org.shop.com.model.Product;
import org.shop.com.domain.service.ProductService;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.io.IOException;


@WebServlet(
        description = "Product servlet",
        urlPatterns = { "/products", "/products/*"}
)
@RequiredArgsConstructor
public class ProductServlet extends HttpServlet {
    private final ProductService productService;
    private final String REGEX_MATCH_FOR_MONGO_ID = "[0-9a-fA-F]{24}$";

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (!method.equals(HttpMethod.PATCH.name())) {
            super.service(req, resp);
            return;
        }

        this.doPatch(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if (!httpServletRequest.getRequestURI().equals("/products")) {
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String jsonResponse = productService.createProduct(httpServletRequest);
        httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().write(jsonResponse);
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if (httpServletRequest.getRequestURI().equals("/products")) {
            String jsonResponse = productService.getProducts();
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.getWriter().write(jsonResponse);
        } else if (httpServletRequest.getRequestURI().matches("/products/" + REGEX_MATCH_FOR_MONGO_ID)) {
            String jsonResponse = productService.getProductDetails(httpServletRequest);
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.getWriter().write(jsonResponse);
        } else {
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if (!httpServletRequest.getRequestURI().matches("/products/" + REGEX_MATCH_FOR_MONGO_ID)) {
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String productId = productService.deleteProduct(httpServletRequest);
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        httpServletResponse.getWriter().println(productId);
    }

    protected void doPatch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if (!httpServletRequest.getRequestURI().matches("/products/" + REGEX_MATCH_FOR_MONGO_ID)) {
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String jsonResponse = productService.updateProduct(httpServletRequest);
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        httpServletResponse.getWriter().write(jsonResponse);
    }
}
