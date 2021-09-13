package com.codecool.shop.dao.memory;


import com.codecool.shop.dao.CheckoutDao;

import java.util.HashMap;

public class CheckoutDaoMem implements CheckoutDao {

    HashMap<String, String> orderData = new HashMap<>();

    private static CheckoutDaoMem instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private CheckoutDaoMem() {
    }

    public static CheckoutDaoMem getInstance() {
        if (instance == null) {
            instance = new CheckoutDaoMem();
        }
        return instance;
    }

    @Override
    public void addDataSegment(String key, String value) {
        orderData.put(key, value);
    }

    public HashMap<String, String> getOrderData() {
        return orderData;
    }
}
