package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.CheckoutDao;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.memory.CheckoutDaoMem;
import com.codecool.shop.dao.memory.OrderDaoMem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        engine.process("product/checkout.html", context, resp.getWriter());
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CheckoutDao checkout = CheckoutDaoMem.getInstance();
        OrderDao orderData = OrderDaoMem.getInstance();
        List<String> dataTitles = List.of("name", "email", "phone-number", "billing-address-country",
                "billing-address-city", "billing-address-zipcode", "billing-address-address",
                        "shipping-address-country", "shipping-address-city", "shipping-address-zipcode",
                        "shipping-address-address");
        dataTitles.forEach(key -> checkout.addDataSegment(key, req.getParameter(key)));
        orderData.getOrder().setShippingDetails(checkout.getOrderData());
        logger.info("Shipping & billing information: {}", orderData.getOrder().getShippingDetails());
        resp.sendRedirect("/payment");
        logger.info("redirecting {}", resp);
    }

}
