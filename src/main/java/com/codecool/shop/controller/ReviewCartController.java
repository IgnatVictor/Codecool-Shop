package com.codecool.shop.controller;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.memory.OrderDaoMem;
import com.codecool.shop.config.TemplateEngineUtil;
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

@WebServlet(urlPatterns = {"/review-cart"})
public class ReviewCartController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OrderDao orderData = OrderDaoMem.getInstance();
        logger.info("Currently there are {} items in the cart, total is: {} USD", orderData.getOrder().totalCartQuantity(), orderData.getOrder().totalCartPrice());

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        context.setVariable("order", orderData.getOrder().getLineItems());
        context.setVariable("ordertotal", orderData.getOrder());
        engine.process("product/review.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OrderDao orderData = OrderDaoMem.getInstance();
        int productId = Integer.parseInt(req.getParameter("line-item-id"));
        String editType = req.getParameter("edit");
        String productName = orderData.getOrder().getLineItem(productId).getProduct().getName();
        switch (editType){
            case "increase":
                orderData.getOrder().incrementLineItem(productId);
                logger.info("Default user | has increased product's quantity in cart: product id: {} | product name: {}", productId, productName);
                break;
            case "decrease":
                orderData.getOrder().decrementLineItem(productId);
                logger.info("Default user | has decreased product's quantity in cart: product id: {} | product name: {}", productId, productName);
                break;
            case "clear":
                orderData.getOrder().setQuantity(productId, 0);
                logger.info("Default user | has removed product from cart: product id: {} | product name: {}", productId, productName);
                break;
            default:
                orderData.getOrder().setQuantity(productId, Integer.parseInt(req.getParameter("edit")));
                logger.info("Default user | has edited product's quantity in cart: product id: {} | product name: {}", productId, productName);
        }
        resp.sendRedirect("/review-cart");
    }
}