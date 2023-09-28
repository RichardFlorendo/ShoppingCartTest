package com.example.shoppingcarttest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ConfirmActivity extends AppCompatActivity {
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmactivity);

        TextView actLabel = findViewById(R.id.label);
        actLabel.setText("Order Confirmation");

        ImageView cartNotif = findViewById(R.id.cartnotif);
        TextView cartNum = findViewById(R.id.cartnum);
        TextView orderNum = findViewById(R.id.ordernumber);

        Button returnBtn = findViewById(R.id.returnBTN);

        Intent intent = getIntent();
        String orderID = intent.getStringExtra("OrderID");
        orderNum.setText(orderID);

        cartNotif.setVisibility(View.INVISIBLE);
        cartNum.setVisibility(View.INVISIBLE);

        returnBtn.setOnClickListener(v -> {
            Intent actintent = new Intent(ConfirmActivity.this, MainActivity.class);
            startActivity(actintent);
        });
    }
}

