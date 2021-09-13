package com.codecool.shop.model;

public class LineItem{
    private int quantity;
    private Product product;
    private float totalPrice;

    public LineItem(Product product) {
        this.quantity = 1;
        this.product = product;
        this.totalPrice = product.getDefaultPrice();
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void incrementQuantity(){
        quantity++;
        adjustTotalPrice();
    }

    public void decrementQuantity(){
        quantity--;
        adjustTotalPrice();
    }

    public void setQuantity(int newQuantity){
        this.quantity = newQuantity;
        adjustTotalPrice();
    }

    private void adjustTotalPrice(){
        totalPrice = quantity * product.getDefaultPrice();
    }

    @Override
    public String toString() {
        return String.format("id: %1$d, " +
                        "name: %2$s, " +
                        "quantity: %3$d",
                this.product.id,
                this.product.name,
                this.quantity);
    }

}
