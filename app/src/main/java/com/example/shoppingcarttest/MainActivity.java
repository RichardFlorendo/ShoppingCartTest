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
    public ArrayList<String> cartList= new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String TAG = "MainActivity";
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView actLabel = findViewById(R.id.label);
        actLabel.setText("Products");

        pref = getSharedPreferences("MyPref", 0); // 0 - for private mode

        ImageButton cartBtn = findViewById(R.id.cart);
        TextView homeTV = findViewById(R.id.homeTV);

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

        cartBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            intent.putExtra("CartList", cartList);
            startActivity(intent);
        });

        homeTV.setOnClickListener(v -> {
            finish();
            startActivity(getIntent());
        });
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
                int item = pref.getInt("p_"+ i + "ID", -1);
                Log.d(TAG, String.valueOf(item));

                if (item > 0){
                    cartList.add(String.valueOf(item));
                }
            }
            return cartList.size();
        }
        else {
            cartNotif.setVisibility(View.INVISIBLE);
            cartNum.setVisibility(View.INVISIBLE);
            return 0;
        }
    }

}