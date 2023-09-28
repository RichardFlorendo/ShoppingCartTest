package com.example.shoppingcarttest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.shoppingcarttest.adapters.CartAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class CheckoutActivity extends AppCompatActivity {
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkoutactivity);
        pref = getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        TextView actLabel = findViewById(R.id.label);
        actLabel.setText("Checkout");

        ImageView cartNotif = findViewById(R.id.cartnotif);
        TextView cartNum = findViewById(R.id.cartnum);

        Button checkOutBtn = findViewById(R.id.confirmPayBTN);

        EditText emailET = findViewById(R.id.emailET);
        Switch tosSW = findViewById(R.id.tosSW);
        EditText nameET = findViewById(R.id.nameET);
        Intent intent = getIntent();
        int total = intent.getIntExtra("Price", 0);

        boolean isCart = pref.getBoolean("isCartEmpty", true);
        int cartItems = pref.getInt("CartItems", -1);

        if (!isCart){
            cartNotif.setVisibility(View.VISIBLE);
            cartNum.setVisibility(View.VISIBLE);

            cartNum.setText(String.valueOf(cartItems));
        }
        else {
            cartNotif.setVisibility(View.INVISIBLE);
            cartNum.setVisibility(View.INVISIBLE);
        }

        checkOutBtn.setText("Pay $" + total);

        final String[] jsonInput = new String[1];
        String randNum = getRandomNumberString();
        ArrayList<String> list = intent.getStringArrayListExtra("CartList");

        checkOutBtn.setOnClickListener(v -> {
            if (nameET.getText() == null || emailET.getText() == null){
                Toast.makeText(getApplicationContext(), "Field Text Empty", Toast.LENGTH_SHORT).show();
            }
            else if (!tosSW.isChecked()){
                Toast.makeText(getApplicationContext(), "Agree to Terms and Conditions", Toast.LENGTH_SHORT).show();
            }
            else{
                jsonInput[0] = "{\""+randNum+"\":{\"fullname\":"+nameET.getText()+"\",\"items\":"+list.toString()+"\",\"total\":\""+total+"}}";
                try {
                    save(this, jsonInput[0], randNum);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                editor.clear();
                editor.commit();

                Intent actintent = new Intent(CheckoutActivity.this, ConfirmActivity.class);
                actintent.putExtra("OrderID", randNum);
                startActivity(actintent);
            }

        });
    }

    public void save(Context context, String jsonString, String randNum) throws IOException {

        String orderID = "order" + randNum;
        FileOutputStream outputStream;

        File file = new File(context.getFilesDir(), "order"+orderID+".json");

        if(file.exists()){
            String newNum = getRandomNumberString();
            save(this, jsonString, newNum);
        }
        else{
            try {
                outputStream = openFileOutput("order"+orderID+".json", Context.MODE_PRIVATE);
                outputStream.write(jsonString.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        return String.format("%06d", number);
    }
}
