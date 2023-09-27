package com.example.shoppingcarttest.datamodel;

import android.content.Context;
import android.util.Log;

import com.example.shoppingcarttest.adapters.ItemAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ItemModel {

    private Context context;

    public ItemModel(Context context) {
        this.context = context;
    }
    private String id;
    private String name;
    private String category;
    private String cost;
    private String color;


    public ItemModel(String id, String name, String category, String cost, String color) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.cost = cost;
        this.color = color;
    }

    public String getProdId() {
        return id;
    }

    public void setProdId(String id) {
        this.id = id;
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
