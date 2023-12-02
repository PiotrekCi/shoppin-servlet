package org.shop.com.domain.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.shop.com.domain.service.OrderService;

import java.io.IOException;

@WebServlet(
        description = "Orders servlet",
        urlPatterns = {"/orders", "/orders/*", "/orders/*/deliverer/*"}
)
@RequiredArgsConstructor
public class OrderServlet extends HttpServlet {
    private final OrderService orderService;
    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String jsonResponse = orderService.createOrder(httpServletRequest);
        httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().write(jsonResponse);
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String method = httpServletRequest.getPathInfo();
        if (method.matches("/\\d+")) {
            getOrderDetails(httpServletRequest, httpServletResponse);
        } else if (method.matches("/\\d+/deliverer")) {
            String action = httpServletRequest.getParameter("action");
            if (action.equals("message")) {
                delivererActionMessage(httpServletRequest, httpServletResponse);
            } else if (action.equals("extend")) {
                delivererActionExtend(httpServletRequest, httpServletResponse);
            } else {
                httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void getOrderDetails(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String jsonResponse = orderService.getOrderDetails(httpServletRequest);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().write(jsonResponse);
    }

    private void delivererActionMessage(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String jsonResponse = orderService.getOrderForAction(httpServletRequest).sendMessage();
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().write(jsonResponse);
    }

    private void delivererActionExtend(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String jsonResponse = orderService.getOrderForAction(httpServletRequest).extendDelivery();
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().write(jsonResponse);
    }
}
