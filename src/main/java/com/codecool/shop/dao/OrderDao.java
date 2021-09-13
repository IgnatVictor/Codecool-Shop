package com.codecool.shop.dao;

import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.Order;

public interface OrderDao {
    void add(LineItem lineItem);

    Order getOrder();
}
