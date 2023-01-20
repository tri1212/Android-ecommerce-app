package com.rmit.multiverseshop.model;

import java.util.HashMap;
import java.util.List;

public class CartItemList {

    private List<HashMap<String, Object>> items;

    public List<HashMap<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<HashMap<String, Object>> items) {
        this.items = items;
    }
}
