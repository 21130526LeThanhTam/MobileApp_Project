package com.example.lab1.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab1.R;
import com.example.lab1.adapter.OrderAdapter;
import com.example.lab1.model.Order;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {
    private ListView orderListView;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        orderListView = findViewById(R.id.orderListView);
        loadOrderHistory();
        ImageView backButton = findViewById(R.id.imageView2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderHistoryActivity.this, CartActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadOrderHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("OrderPrefs", MODE_PRIVATE);
        String ordersJson = sharedPreferences.getString("orders", null);

        List<Order> orders;
        if (ordersJson != null) {
            Type type = new TypeToken<List<Order>>() {}.getType();
            orders = new Gson().fromJson(ordersJson, type);
        } else {
            orders = new ArrayList<>();
        }

        orderAdapter = new OrderAdapter(this, orders);
        orderListView.setAdapter(orderAdapter);
    }
}