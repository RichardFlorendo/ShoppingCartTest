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
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView cartList;
    private int total = 0;
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

        TextView homeTV = findViewById(R.id.homeTV);
        Button checkBtn = findViewById(R.id.checkoutBTN);

        Intent intent = getIntent();
        ArrayList<String> list = intent.getStringArrayListExtra("CartList");

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

            Log.d(TAG, String.valueOf(list.size()));

            for (int i=0; i <list.size(); i++){
                addItemsFromJSON(i);
            }
        }
        else {
            cartNotif.setVisibility(View.INVISIBLE);
            cartNum.setVisibility(View.INVISIBLE);
        }

        checkBtn.setOnClickListener(v -> {
            Intent checkintent = new Intent(CartActivity.this, CheckoutActivity.class);
            checkintent.putExtra("CartList", list);
            checkintent.putExtra("Price", total);
            startActivity(checkintent);
        });

        homeTV.setOnClickListener(v -> startActivity(new Intent(CartActivity.this, MainActivity.class)));
    }

    private void addItemsFromJSON(int index) {
        try {
            TextView cartTotal = findViewById(R.id.cartTotal);

            JSONObject object = new JSONObject(readJSONDataFromFile());
            JSONArray jsonArray  = object.getJSONArray("products");

            String id = String.valueOf((index + 1));

            int idRef = pref.getInt("p_"+ id + "ID", -1);
            Log.d(TAG, String.valueOf(idRef));

            JSONObject itemObj = jsonArray.getJSONObject(idRef);
            Log.d("TestingTesting", String.valueOf(index + idRef));

            String itemName = itemObj.getString("name");
            String itemCost = itemObj.getString("price");
            String bgColor = itemObj.getString("bgColor");
            Log.d("Testing", itemName + "ID");

            total += Integer.parseInt(itemCost.substring(0, itemCost.indexOf(".")));
            cartTotal.setText("$" + total);

            CartModel cartModel = new CartModel(String.valueOf(idRef), itemName, itemCost, bgColor);
            viewItems.add(cartModel);
        } catch (JSONException | IOException e) {
            Log.d(TAG, "addItemsFromJSON: ", e);
        }
    }

    private String readJSONDataFromFile() throws IOException{

        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try {

            String jsonString;
            inputStream = getResources().openRawResource(R.raw.products);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));

            while ((jsonString = bufferedReader.readLine()) != null) {
                builder.append(jsonString);
            }

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return new String(builder);
    }

    public void refreshActivtiy(){
        recreate();
    }
}
