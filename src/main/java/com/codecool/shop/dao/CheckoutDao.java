package com.codecool.shop.dao;


import java.util.HashMap;

public interface CheckoutDao {

    void addDataSegment(String key, String value);

    HashMap<String, String> getOrderData();

}
