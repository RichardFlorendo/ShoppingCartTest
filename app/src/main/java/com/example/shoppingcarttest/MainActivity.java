package com.example.shoppingcarttest;

import static android.app.PendingIntent.getActivity;

import com.example.shoppingcarttest.adapters.ItemAdapter;
import com.example.shoppingcarttest.datamodel.CartModel;
import com.example.shoppingcarttest.datamodel.ItemModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView itemList;
    private List<Object> viewItems = new ArrayList<>();
    List<String> cartList= new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String TAG = "MainActivity";
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView actLabel = findViewById(R.id.label);
        actLabel.setText("Shop");

        pref = getSharedPreferences("MyPref", 0); // 0 - for private mode

        ImageButton cartBtn = findViewById(R.id.cart);
        Button homeBtn = findViewById(R.id.homeBTN);

        TextView cartNum = findViewById(R.id.cartnum);

        int cartItems = checkCart();

        if (cartItems > 0){
            cartNum.setText(String.valueOf(cartItems));
            SharedPreferences.Editor editor = pref.edit();

            editor.putInt("CartItems", cartItems);
            editor.commit();
        }


        itemList = findViewById(R.id.itemlist);

        layoutManager = new LinearLayoutManager(this);
        itemList.setLayoutManager(layoutManager);

        mAdapter = new ItemAdapter(this, viewItems);
        itemList.setAdapter(mAdapter);

        addItemsFromJSON();

        cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));

        homeBtn.setOnClickListener(v -> {
            finish();
            startActivity(getIntent());
        });
    }

    public List<String> getList() {
        return cartList;
    }

    private void addItemsFromJSON() {
        try {

            JSONObject object = new JSONObject(readJSONDataFromFile());
            JSONArray jsonArray  = object.getJSONArray("products");

            for (int i=0; i<jsonArray.length(); ++i) {

                JSONObject itemObj = jsonArray.getJSONObject(i);

                String idVal = itemObj.getString("id");
                String nameVal = itemObj.getString("name");
                String categVal = itemObj.getString("category");
                String priceVal = itemObj.getString("price");
                String colorVal = itemObj.getString("bgColor");

                ItemModel itemModel = new ItemModel(idVal, nameVal, categVal, priceVal, colorVal);
                viewItems.add(itemModel);
            }

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

    public int checkCart(){
        ImageView cartNotif = findViewById(R.id.cartnotif);
        TextView cartNum = findViewById(R.id.cartnum);

        boolean isCart = pref.getBoolean("isCartEmpty", true);

        if (!isCart){
            cartNotif.setVisibility(View.VISIBLE);
            cartNum.setVisibility(View.VISIBLE);

            for (int i=1; i < 6; i++){
                String item = pref.getString("p_"+ i + "ID", null);
                if (item != null){
                    cartList.add(item);
                }
            }

            Log.d("Testing", cartList.toString());
            return cartList.size();
        }
        else {
            cartNotif.setVisibility(View.INVISIBLE);
            cartNum.setVisibility(View.INVISIBLE);
            return 0;
        }
    }

}