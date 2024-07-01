//package com.example.lab1.activity;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.AppCompatButton;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.lab1.R;
//import com.example.lab1.adapter.CartAdapter;
//import com.example.lab1.model.Cart;
//import com.example.lab1.model.CartItem;
//import com.example.lab1.model.Order;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//
//public class CartActivity extends AppCompatActivity {
//    private Cart cart;
//    private ListView cartListView;
//    private TextView totalPriceTextView;
//    private TextView totalFeeTextView;
//    private CartAdapter adapter;
//    private AppCompatButton checkOutBtn;
//    private AppCompatButton viewOrderHistoryBtn;
//    private EditText recipientNameEditText;
//    private EditText recipientPhoneEditText;
//    private EditText recipientAddressEditText;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
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
//        loadCartItems();
//
//        adapter = new CartAdapter(this, cart.getCartItems(), new CartAdapter.OnQuantityChangeListener() {
//            @Override
//            public void onQuantityChanged() {
//                saveCartItems();
//                updateTotalPrice();
//                updateTotalQuantity();
//            }
//        });
//        cartListView.setAdapter(adapter);
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
//
//        updateTotalPrice();
//        updateTotalQuantity();
//    }
//
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
//
//    private void saveCartItems() {
//        SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        List<CartItem> cartItems = cart.getCartItems();
//        String cartJson = new Gson().toJson(cartItems);
//        editor.putString("cart_items", cartJson);
//        editor.apply();
//    }
//
//    private void updateTotalPrice() {
//        double totalPrice = cart.getTotalPrice();
//        totalPriceTextView.setText(String.format("$%.2f", totalPrice));
//    }
//
//    private void updateTotalQuantity() {
//        int totalQuantity = 0;
//        for (CartItem item : cart.getCartItems()) {
//            totalQuantity += item.getQuantity();
//        }
//        totalFeeTextView.setText(String.format("Total Quantity: %d", totalQuantity));
//    }
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
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Reload the cart items and update the UI
//        loadCartItems();
//        adapter.updateCartItems(cart.getCartItems());
//        updateTotalPrice();
//        updateTotalQuantity();
//    }
//
//    private void placeOrder() {
//
//        String recipientName = recipientNameEditText.getText().toString();
//        String recipientPhone = recipientPhoneEditText.getText().toString();
//        String recipientAddress = recipientAddressEditText.getText().toString();
//
//        if (recipientName.isEmpty() || recipientPhone.isEmpty() || recipientAddress.isEmpty()) {
//            Toast.makeText(this, "Hãy điền thông tin giao hàng", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        SharedPreferences sharedPreferences = getSharedPreferences("OrderPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        String ordersJson = sharedPreferences.getString("orders", null);
//
//        List<Order> orders;
//        if (ordersJson != null) {
//            Type type = new TypeToken<List<Order>>() {}.getType();
//            orders = new Gson().fromJson(ordersJson, type);
//        } else {
//            orders = new ArrayList<>();
//        }
//
//        Order newOrder = new Order(cart., cart.getTotalPrice(),recipientName, recipientPhone, recipientAddress);
//        orders.add(newOrder);
//
//        String newOrdersJson = new Gson().toJson(orders);
//        editor.putString("orders", newOrdersJson);
//        editor.apply();
//
//        // Clear cart after placing order
//        cart.clearCart();
//        saveCartItems();
//        adapter.updateCartItems(cart.getCartItems());
//        updateTotalPrice();
//        updateTotalQuantity();
//        Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
//    }
//}