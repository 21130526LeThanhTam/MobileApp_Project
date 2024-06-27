package com.example.lab1.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.bumptech.glide.Glide;

import com.example.lab1.model.Cart;
import com.example.lab1.model.CartItem;
import com.example.lab1.model.NewProduct;
import com.example.lab1.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    TextView tensp, giasp, mota;
    Button btnThem;
    ImageView hinhanh;
    Spinner spinner;
    Toolbar toolbar;

    private Cart cart;
    private CartItem cartItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        initView();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initData();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });
    }

    private void initData() {
        NewProduct newProduct = (NewProduct) getIntent().getSerializableExtra("chitiet");
        tensp.setText(newProduct.getName());
        mota.setText(newProduct.getDescription());
        Glide.with(getApplicationContext()).load(newProduct.getImage()).into(hinhanh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText("Giá: " + decimalFormat.format(Double.parseDouble(newProduct.getPrice())) + "đ");

        // Khởi tạo CartItem
        cartItem = new CartItem(
                String.valueOf(newProduct.getId()),
                newProduct.getName(),
                1,  // Số lượng mặc định là 1
                newProduct.getPrice(),
                newProduct.getImage()
        );

        // Tải giỏ hàng từ SharedPreferences
        cart = loadCart();
    }

    private void initView() {
        tensp = findViewById(R.id.txt_tensp);
        giasp = findViewById(R.id.txt_giasp);
        mota = findViewById(R.id.txtmota_chitiet);
        hinhanh = findViewById(R.id.img_chitiet);
        btnThem = findViewById(R.id.btn_themgiohang);
        spinner = findViewById(R.id.spinner);
        toolbar = findViewById(R.id.toolbar);
    }

    private Cart loadCart() {
        SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        String cartJson = sharedPreferences.getString("cart_items", null);
        Cart cart = new Cart();
        if (cartJson != null) {
            Type type = new TypeToken<List<CartItem>>() {}.getType();
            List<CartItem> cartItems = new Gson().fromJson(cartJson, type);
            for (CartItem item : cartItems) {
                cart.addToCart(item);
            }
        }
        return cart;
    }

    private void saveCartItems() {
        SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        List<CartItem> cartItems = cart.getCartItems();
        String cartJson = new Gson().toJson(cartItems);
        editor.putString("cart_items", cartJson);
        editor.apply();
    }

    private void addToCart() {
        cart.addToCart(cartItem);
        saveCartItems();
        Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
    }
}