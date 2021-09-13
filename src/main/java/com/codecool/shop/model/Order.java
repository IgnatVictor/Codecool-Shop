package com.codecool.shop.model;

import java.util.HashMap;
import java.util.HashSet;

public class Order {
    private HashSet<LineItem> lineItems = new HashSet<>();
    private HashMap<String, String> shippingDetails = new HashMap<>();

    public HashSet<LineItem> getLineItems() {
        return lineItems;
    }

    public HashMap<String, String> getShippingDetails() {
        return shippingDetails;
    }

    public boolean containsLineItem(int id){
        for (LineItem item : lineItems){
            if(id == item.getProduct().getId()){
                return true;
            }
        }
        return false;
    }

    public void setShippingDetails(HashMap<String, String> shippingDetails) {
        this.shippingDetails = shippingDetails;
    }

    public void addNewLineItem(LineItem lineItem){
        lineItems.add(lineItem);
    }

    public LineItem getLineItem(int id) {
        return lineItems.stream().filter(lineItem -> id == lineItem.getProduct().getId()).findFirst().orElse(null);
    }

    public void incrementLineItem(int id){
        for (LineItem item : lineItems){
            if(id == item.getProduct().getId()){
                item.incrementQuantity();
            }
        }
    }

    public void decrementLineItem(int id){
        for (LineItem item : lineItems){
            if(id == item.getProduct().getId()){
                item.decrementQuantity();
            }
        }
        removeAllZeroQuantities();
    }

    public void setQuantity(int id, int quantity){
        for (LineItem item : lineItems){
            if(id == item.getProduct().getId()){
                item.setQuantity(quantity);
            }
        }
        removeAllZeroQuantities();
    }

    private void removeAllZeroQuantities() {
        lineItems.removeIf(item -> item.getQuantity() == 0);
    }

    public int totalCartQuantity(){
        int total = 0;
        for (LineItem item : lineItems){
            total += item.getQuantity();
        }
        return total;
    }

    public float totalCartPrice(){
        float total = 0;
        for (LineItem item : lineItems){
            total += item.getTotalPrice();
        }
        return total;
    }
}
