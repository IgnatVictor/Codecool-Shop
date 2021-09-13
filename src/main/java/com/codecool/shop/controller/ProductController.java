package com.codecool.shop.controller;

import com.codecool.shop.config.ConnectionManager;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.jdbc.ProductCategoryDaoJdbc;
import com.codecool.shop.dao.jdbc.ProductDaoJdbc;
import com.codecool.shop.dao.jdbc.SupplierDaoJdbc;
import com.codecool.shop.dao.memory.OrderDaoMem;
import com.codecool.shop.dao.memory.ProductCategoryDaoMem;
import com.codecool.shop.dao.memory.ProductDaoMem;
import com.codecool.shop.dao.memory.SupplierDaoMem;
import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.service.ProductService;
import com.codecool.shop.config.TemplateEngineUtil;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;


@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {
    private ProductDao productDataStore;
    private ProductCategoryDao productCategoryDataStore;
    private SupplierDao supplier;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (ConnectionManager.getDaoType().equals("jdbc")) {
            try {
                DataSource dataSource = ConnectionManager.connect();
                productCategoryDataStore = new ProductCategoryDaoJdbc(dataSource);
                supplier = new SupplierDaoJdbc(dataSource);
                productDataStore = new ProductDaoJdbc(dataSource, productCategoryDataStore, supplier);
            } catch (SQLException throwables) {
                logger.error("Database connection error", throwables);
            }
        } else {
            productDataStore = ProductDaoMem.getInstance();
            productCategoryDataStore = ProductCategoryDaoMem.getInstance();
            supplier = SupplierDaoMem.getInstance();
        }

        ProductService productService = new ProductService(productDataStore, productCategoryDataStore);
        OrderDao orderData = OrderDaoMem.getInstance();

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        List<ProductCategory> categories = productCategoryDataStore.getAll();
        context.setVariable("products", productService.getProductsForCategory(1));
        context.setVariable("cart", orderData.getOrder().totalCartQuantity());
        context.setVariable("categories", categories);
        String category = req.getParameter("category");
        if (category != null) {
            context.setVariable("products", productDataStore.getBy(productCategoryDataStore.find(category)));
        }


        List<Supplier> suppliers = supplier.getAll();
        context.setVariable("suppliers", suppliers);
        String supplierr = req.getParameter("supplier");
        if (supplierr != null) {
            context.setVariable("products", productDataStore.getBy(supplier.find(supplierr)));
        }

        engine.process("product/index.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OrderDao orderData = OrderDaoMem.getInstance();
        if (ConnectionManager.getDaoType().equals("jdbc")) {
            try {
                DataSource dataSource = ConnectionManager.connect();
                productDataStore = new ProductDaoJdbc(dataSource, productCategoryDataStore, supplier);
            } catch (SQLException throwables) {
                logger.error("Database connection error", throwables);
            }
        }else{
        productDataStore = ProductDaoMem.getInstance();
        }

        int productId = Integer.parseInt(req.getParameter("productId"));
        if (orderData.getOrder().containsLineItem(productId)) {
            orderData.getOrder().incrementLineItem(productId);
            logger.info("Default user | has added one more product to the cart: product id: {} | product name: {}", productId, productDataStore.find(productId).getName());
        } else {
            orderData.add(new LineItem(productDataStore.find(productId)));
            logger.info("Default user | has added one new product to the cart: product id: {} | product name: {}", productId, productDataStore.find(productId).getName());
        }

        resp.sendRedirect(getCurrentUrl(req));
    }

    private String getCurrentUrl(HttpServletRequest req) {
        StringBuilder requestURL = new StringBuilder(req.getRequestURL().toString());
        String queryString = req.getQueryString();

        if (queryString == null) {
            return req.getRequestURI();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }
}
