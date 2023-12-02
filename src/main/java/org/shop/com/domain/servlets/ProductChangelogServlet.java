package org.shop.com.domain.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.shop.com.domain.service.ProductChangelogService;
import org.springframework.http.MediaType;

import java.io.IOException;

@WebServlet(
        description = "ProductChangelog servlet",
        urlPatterns = { "/products-changelog/*" }
)
@RequiredArgsConstructor
public class ProductChangelogServlet extends HttpServlet {
    private final ProductChangelogService productChangelogService;
    private final String REGEX_MATCH_FOR_MONGO_ID = "[0-9a-fA-F]{24}$";

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if (!httpServletRequest.getRequestURI().matches("/products-changelog/" + REGEX_MATCH_FOR_MONGO_ID)) {
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String jsonResponse = productChangelogService.getProductChangelog(httpServletRequest);
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        httpServletResponse.getWriter().println(jsonResponse);
    }
}
