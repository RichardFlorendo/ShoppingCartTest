package com.example.shoppingcarttest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingcarttest.adapters.CartAdapter;
import com.example.shoppingcarttest.adapters.ItemAdapter;
import com.example.shoppingcarttest.datamodel.CartModel;
import com.example.shoppingcarttest.datamodel.ItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private MainActivity mainActivity;
    private List<Object> viewItems = new ArrayList<>();
    private static final String TAG = "CartActivity";
    List<String> list= new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView cartList;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cartactivity);
        pref = getSharedPreferences("MyPref", 0); // 0 - for private mode

        TextView actLabel = findViewById(R.id.label);
        actLabel.setText("Cart");

        ImageView cartNotif = findViewById(R.id.cartnotif);
        TextView cartNum = findViewById(R.id.cartnum);

        Button homeBtn = findViewById(R.id.homeBTN);

        pref = getSharedPreferences("MyPref", 0);

        mainActivity = new MainActivity();
        List<String> list = mainActivity.getList();

        cartList = findViewById(R.id.cartList);

        layoutManager = new LinearLayoutManager(this);
        cartList.setLayoutManager(layoutManager);

        mAdapter = new CartAdapter(this, viewItems);
        cartList.setAdapter(mAdapter);

        int cartItems = pref.getInt("CartItems", -1);

        if (cartItems > 0){
            cartNotif.setVisibility(View.VISIBLE);
            cartNum.setVisibility(View.VISIBLE);

            cartNum.setText(String.valueOf(cartItems));

            for (int i=0; i <list.size(); i++){
                String idRef = pref.getString("p_"+ i + "ID", null);

                String itemName = pref.getString(idRef + "ItemName", null);
                String itemCost = pref.getString(idRef + "ItemCost", null);
                String bgColor = pref.getString(idRef + "BgColor", null);

                CartModel cartModel = new CartModel(idRef, itemName, itemCost, bgColor);
                viewItems.add(cartModel);
            }

            cartNum.setText(String.valueOf(list.size()));
        }
        else {
            cartNotif.setVisibility(View.INVISIBLE);
            cartNum.setVisibility(View.INVISIBLE);
        }

        homeBtn.setOnClickListener(v -> startActivity(new Intent(CartActivity.this, MainActivity.class)));
    }
}
