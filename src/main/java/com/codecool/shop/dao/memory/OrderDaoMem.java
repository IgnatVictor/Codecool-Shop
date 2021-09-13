package com.codecool.shop.dao.memory;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.Order;

public class OrderDaoMem implements OrderDao {

    private Order order = new Order();
    private static OrderDaoMem instance = null;

    public OrderDaoMem() {
    }

    public static OrderDaoMem getInstance() {
        if (instance == null) {
            instance = new OrderDaoMem();
        }
        return instance;
    }

    @Override
    public void add(LineItem lineItem) {
        order.addNewLineItem(lineItem);
    }

    @Override
    public Order getOrder() {
        return order;
    }
}
