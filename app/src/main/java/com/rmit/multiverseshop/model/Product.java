package com.rmit.multiverseshop.model;

public class Product {
    private String imageUrl;
    private String name;
    private String category;
    private String lowercasedName;
    private int productsSold;
    private double price;

    public Product(String imageUrl, String name, String category, String lowercasedName, int productsSold, double price) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.category = category;
        this.lowercasedName = lowercasedName;
        this.productsSold = productsSold;
        this.price = price;
    }

    public Product() {}

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

    public String getLowercasedName() {
        return lowercasedName;
    }

    public void setLowercasedName(String lowercasedName) {
        this.lowercasedName = lowercasedName;
    }
}
