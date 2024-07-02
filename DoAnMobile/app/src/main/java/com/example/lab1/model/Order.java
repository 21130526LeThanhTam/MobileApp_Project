package com.example.lab1.model;

import java.util.List;

public class Order {
    private List<CartItem> products;
    private String orderDate;
    private String recipientName;
    private String recipientPhone;
    private String recipientAddress;
    private String orderStatus;
    private String userId;

    public Order(List<CartItem> products, String orderDate, String recipientName, String recipientPhone, String recipientAddress, String oderStatus, String userId) {
        this.products = products;
        this.orderDate = orderDate;
        this.recipientName = recipientName;
        this.recipientPhone = recipientPhone;
        this.recipientAddress = recipientAddress;
        this.orderStatus = oderStatus;
        this.userId = userId;
    }

    public Order() {
    }

    public List<CartItem> getProducts() {
        return products;
    }

    public void setProducts(List<CartItem> products) {
        this.products = products;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOderStatus(String oderStatus) {
        this.orderStatus = oderStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}