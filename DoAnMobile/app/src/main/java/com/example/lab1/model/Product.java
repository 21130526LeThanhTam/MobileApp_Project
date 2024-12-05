package com.example.lab1.model;

import java.text.DecimalFormat;

public class Product {
    private int id;
    private String name_product;
    private int image_product;
    private int price;

    public Product(int id, String name_product, int image_product, int price) {
        this.id = id;
        this.name_product = name_product;
        this.image_product = image_product;
        this.price = price;
    }

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public int getImage_product() {
        return image_product;
    }

    public void setImage_product(int image_product) {
        this.image_product = image_product;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String formatPrice() {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedPrice = decimalFormat.format(price);
        return formattedPrice.replace(',', '.')+"đ";
    }


}
