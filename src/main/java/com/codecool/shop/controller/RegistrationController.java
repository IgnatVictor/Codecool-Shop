package com.codecool.shop.controller;

import com.codecool.shop.config.ConnectionManager;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.UserDao;
import com.codecool.shop.dao.jdbc.UserDaoJdbc;
import com.codecool.shop.dao.memory.UserDaoMem;
import com.codecool.shop.model.User;
import com.codecool.shop.util.MailSender;
import org.slf4j.Logger;
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

@WebServlet(urlPatterns = {"/register"})
public class RegistrationController extends HttpServlet {
    private UserDao userData;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        engine.process("user/register.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (ConnectionManager.getDaoType().equals("jdbc")) {
            try {
                DataSource dataSource = ConnectionManager.connect();
                userData = new UserDaoJdbc(dataSource);
            } catch (SQLException throwables) {
                logger.error("Database connection error", throwables);
            }
        }else{
            userData = UserDaoMem.getInstance();
        }
        String name = req.getParameter("name");
        String emailAddress = req.getParameter("email");
        String password = req.getParameter("password");
        userData.add(new User(name, emailAddress, password));
        MailSender.sendConfirmation(emailAddress, name);
        resp.sendRedirect("/");
    }
}