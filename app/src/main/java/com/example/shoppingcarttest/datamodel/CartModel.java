package com.example.shoppingcarttest.datamodel;

public class CartModel {
    private String id;
    private String item;
    private String cost;
    private String color;


    public CartModel(String id, String item, String cost, String color) {
        this.id = id;
        this.item = item;
        this.cost = cost;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
