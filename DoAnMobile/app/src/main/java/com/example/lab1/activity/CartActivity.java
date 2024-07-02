package com.example.lab1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cart);
//
//        cartListView = findViewById(R.id.cartView);
//        totalPriceTextView = findViewById(R.id.totalTxt);
//        totalFeeTextView = findViewById(R.id.totalFeeTxt);
//        checkOutBtn = findViewById(R.id.checkOutBtn);
//        viewOrderHistoryBtn = findViewById(R.id.viewOrderHistoryBtn);
//        recipientNameEditText = findViewById(R.id.recipientName);
//        recipientPhoneEditText = findViewById(R.id.recipientPhone);
//        recipientAddressEditText = findViewById(R.id.recipientAddress);
//
////        loadCartItems();
//
//        cartItemList = new ArrayList<>();
//        adapter = new CartAdapter(this, R.layout.cart_item, cartItemList);
//        cartListView.setAdapter(adapter);
//
//        // Khởi tạo Firebase
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//
//        if (currentUser != null) {
//            String userId = currentUser.getUid();
//            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//            cartRef = firebaseDatabase.getReference("User").child(userId).child("cart");
//
//            // Lấy dữ liệu giỏ hàng từ Firebase
//            cartRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    cartItemList.clear();
//                    Cart cart = dataSnapshot.getValue(Cart.class);
//                    if (cart != null && cart.getCartItems() != null) {
//                        cartItemList.addAll(cart.getCartItems());
//                    }
//                    adapter.notifyDataSetChanged();
//                    calculateTotal();
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Toast.makeText(CartActivity.this, "Failed to load cart: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });


//
//        }
//
//        ImageView backButton = findViewById(R.id.imageView);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CartActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        checkOutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                placeOrder();
//            }
//        });
//
//        viewOrderHistoryBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CartActivity.this, OrderHistoryActivity.class);
//                startActivity(intent);
//            }
//        });

//        updateTotalPrice();
//        updateTotalQuantity();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListView = findViewById(R.id.cartView);
        totalPriceTextView = findViewById(R.id.totalTxt);
        totalFeeTextView = findViewById(R.id.totalFeeTxt);
        checkOutBtn = findViewById(R.id.checkOutBtn);
        viewOrderHistoryBtn = findViewById(R.id.viewOrderHistoryBtn);
        recipientNameEditText = findViewById(R.id.recipientName);
        recipientPhoneEditText = findViewById(R.id.recipientPhone);
        recipientAddressEditText = findViewById(R.id.recipientAddress);

        cartItemList = new ArrayList<>();
        adapter = new CartAdapter(this, R.layout.cart_item, cartItemList);
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
                checkout();
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

//    public void loadCartItems() {
//        SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE);
//        String cartJson = sharedPreferences.getString("cart_items", null);
//        if (cartJson != null) {
//            Type type = new TypeToken<List<CartItem>>() {}.getType();
//            List<CartItem> cartItems = new Gson().fromJson(cartJson, type);
//            cart = new Cart(cartItems);
//        } else {
//            cart = new Cart();
//        }
//    }

//    private void saveCartItems() {
//        SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        List<CartItem> cartItems = cart.getCartItems();
//        String cartJson = new Gson().toJson(cartItems);
//        editor.putString("cart_items", cartJson);
//        editor.apply();
//    }

//    private void updateTotalPrice() {
//        double totalPrice = cart.getTotalPrice();
//        totalPriceTextView.setText(String.format("$%.2f", totalPrice));
//    }

    private void updateTotalQuantity() {
        int totalQuantity = 0;
        for (CartItem item : cartItemList) {
            totalQuantity += item.getQuantity();
        }
        totalFeeTextView.setText(String.format("%d", totalQuantity));
    }


//
//    public void addToCart(CartItem item) {
//        cart.addToCart(item);
//        saveCartItems();
//        adapter.notifyDataSetChanged();
//        updateTotalPrice();
//        updateTotalQuantity();
//    }
//
//    public void removeFromCart(CartItem item) {
//        cart.removeFromCart(item);
//        saveCartItems();
//        adapter.notifyDataSetChanged();
//        updateTotalPrice();
//        updateTotalQuantity();
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Reload the cart items and update the UI
//        loadCartItems();
//        adapter.updateCartItems(cart.getCartItems());
//        updateTotalPrice();
//        updateTotalQuantity();
//    }

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

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String orderId = ordersRef.push().getKey();
        double price = calculateTotal();

        Order order = new Order( cartItemList, price, recipientName, recipientPhone, recipientAddress, userId, "Đang chờ xác nhận");
        ordersRef.child(orderId).setValue(order)
                .addOnSuccessListener(aVoid -> {
                    cartRef.removeValue(); // Clear the cart
                    Toast.makeText(CartActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the cart activity
                })
                .addOnFailureListener(e -> Toast.makeText(CartActivity.this, "Failed to place order: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

//    private void placeOrder() {
//        String recipientName = recipientNameEditText.getText().toString().trim();
//        String recipientPhone = recipientPhoneEditText.getText().toString().trim();
//        String recipientAddress = recipientAddressEditText.getText().toString().trim();
//
//        // Kiểm tra thông tin người nhận đã nhập đầy đủ
//        if (recipientName.isEmpty() || recipientPhone.isEmpty() || recipientAddress.isEmpty()) {
//            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin người nhận", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Tạo một đơn hàng mới từ giỏ hàng hiện tại
//        Order newOrder = new Order(cart.getCartItems(), cart.getTotalPrice(), recipientName, recipientPhone, recipientAddress);
//
//        // Lưu đơn hàng vào Firebase
//        DatabaseReference ordersRef = firebaseDatabase.getReference("Order");
//        String orderId = ordersRef.push().getKey();
//        ordersRef.child(orderId).setValue(newOrder)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            // Đặt hàng thành công, thông báo và xóa giỏ hàng
//                            Toast.makeText(CartActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
//                            cart.clearCart();
//                            saveCartItems();
//                            adapter.updateCartItems(cart.getCartItems());
//                            updateTotalPrice();
//                            updateTotalQuantity();
//
//                            // Chuyển về màn hình chính hoặc màn hình order history
//                            startActivity(new Intent(CartActivity.this, MainActivity.class));
//                            finish();
//                        } else {
//                            // Đặt hàng thất bại, thông báo lỗi
//                            Toast.makeText(CartActivity.this, "Đặt hàng không thành công: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
}