package com.rmit.multiverseshop.model;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class Product implements Serializable {

    @DocumentId
    private String id;
    private String imageUrl;
    private String name;
    private String category;
    private int productsSold;
    private double price;

    public Product(String imageUrl, String name, String category, int productsSold, double price) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.category = category;
        this.productsSold = productsSold;
        this.price = price;
    }

    public Product() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getProductsSold() {
        return productsSold;
    }

    public void setProductsSold(int productsSold) {
        this.productsSold = productsSold;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
