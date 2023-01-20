package com.rmit.multiverseshop.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Order {

    final String id = UUID.randomUUID().toString();
    String fullname;
    String address;
    String phone;
    Date date;
    double total;
    List<CartItem> orderItems;

    public Order() {}

    public Order(String fullname, String address, String phone,
                 Date date, double total, List<CartItem> orderItems) {
        this.fullname = fullname;
        this.address = address;
        this.phone = phone;
        this.date = date;
        this.total = total;
        this.orderItems = orderItems;
    }

    public String getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<CartItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<CartItem> orderItems) {
        this.orderItems = orderItems;
    }
}
