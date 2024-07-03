package com.example.lab1.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.lab1.R;
import com.example.lab1.adapter.CartAdapter;
import com.example.lab1.model.Cart;
import com.example.lab1.model.CartItem;
import com.example.lab1.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private Cart cart;
    private ListView cartListView;
    private TextView totalPriceTextView;
    private TextView totalFeeTextView;
    private CartAdapter adapter;
    private AppCompatButton checkOutBtn;
    private AppCompatButton viewOrderHistoryBtn;
    private EditText recipientNameEditText;
    private EditText recipientPhoneEditText;
    private EditText recipientAddressEditText;
    private List<CartItem> cartItemList;
    private DatabaseReference cartRef;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        EdgeToEdge.enable(this);

        cartListView = findViewById(R.id.cartView);
        totalPriceTextView = findViewById(R.id.totalTxt);
        totalFeeTextView = findViewById(R.id.totalFeeTxt);
        checkOutBtn = findViewById(R.id.checkOutBtn);
        viewOrderHistoryBtn = findViewById(R.id.viewOrderHistoryBtn);
        recipientNameEditText = findViewById(R.id.recipientName);
        recipientPhoneEditText = findViewById(R.id.recipientPhone);
        recipientAddressEditText = findViewById(R.id.recipientAddress);


        cartItemList = new ArrayList<>();
        adapter = new CartAdapter(this, cartItemList);
        cartListView.setAdapter(adapter);

        // Khởi tạo Firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            cartRef = firebaseDatabase.getReference("User").child(userId).child("cart");
            ordersRef = firebaseDatabase.getReference("Order");

            // Lấy dữ liệu giỏ hàng từ Firebase
            cartRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    cartItemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        CartItem cartItem = snapshot.getValue(CartItem.class);
                        cartItemList.add(cartItem);
                    }
                    adapter.notifyDataSetChanged();
                    calculateTotal();
                    updateTotalQuantity();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(CartActivity.this, "Failed to load cart: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }

        ImageView backButton = findViewById(R.id.imageView);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFinishing()) {
                    checkout();
                    restartCartActivity();
                }
            }
        });
        viewOrderHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
            }
        });
        calculateTotal();
        updateTotalQuantity();
    }

    private void updateTotalQuantity() {
        int totalQuantity = 0;
        for (CartItem item : cartItemList) {
            totalQuantity += item.getQuantity();
        }
        totalFeeTextView.setText(String.format("%d", totalQuantity));


    }
    private void restartCartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    private double calculateTotal() {
        double total = 0;
        for (CartItem cartItem : cartItemList) {
            total += cartItem.getPrice() * cartItem.getQuantity();
        }
        totalPriceTextView.setText(String.format("$%.2f", total));
        return total;
    }

    private void checkout() {
        if (cartItemList.isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        String recipientName = recipientNameEditText.getText().toString().trim();
        String recipientPhone = recipientPhoneEditText.getText().toString().trim();
        String recipientAddress = recipientAddressEditText.getText().toString().trim();

        // Kiểm tra thông tin người nhận đã nhập đầy đủ
        if (recipientName.isEmpty() || recipientPhone.isEmpty() || recipientAddress.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin người nhận", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String orderId = ordersRef.push().getKey();
        double price = calculateTotal();

        Order order = new Order(orderId, cartItemList, price, recipientName, recipientPhone, recipientAddress, userId, "Đang xử lý");
        ordersRef.child(orderId).setValue(order)
                .addOnSuccessListener(aVoid -> {
                    cartRef.removeValue(); // Clear the cart
                    Toast.makeText(CartActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(CartActivity.this, "Failed to place order: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }


}