package com.example.lab1.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.bumptech.glide.Glide;

import com.example.lab1.model.CartItem;
import com.example.lab1.model.NewProduct;
import com.example.lab1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    TextView tensp, giasp, mota;
    Button btnThem;
    ImageView hinhanh;
    Spinner spinner;
    Toolbar toolbar;

    private DatabaseReference cartRef;
    private CartItem cartItem;
    private List<CartItem> cartItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        initView();

        // Khởi tạo Firebase Database
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            cartRef = firebaseDatabase.getReference("User")
                    .child(userId)
                    .child("cart");

            if (cartItemList == null) {
                cartItemList = new ArrayList<>(); // Khởi tạo nếu chưa có
            }
            // Lấy dữ liệu giỏ hàng từ Firebase
            cartRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    cartItemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        CartItem cartItem = snapshot.getValue(CartItem.class);
                        cartItemList.add(cartItem);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(DetailActivity.this, "Failed to load cart: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


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

        // Khởi tạo CartItem với cartItemId là newProduct.getId()
        cartItem = new CartItem(
                String.valueOf(newProduct.getId()),
                String.valueOf(newProduct.getId()),  // Đã thay đổi productId thành int
                newProduct.getName(),
                1,  // Số lượng mặc định là 1
                Double.parseDouble(newProduct.getPrice()),
                newProduct.getImage()
        );
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

    private void addToCart() {

        // Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng chưa
        boolean isProductExists = false;
        for (CartItem item : cartItemList) {
            if (item.getProductId().equals(cartItem.getProductId())) {
                // Sản phẩm đã tồn tại, cập nhật số lượng
                int newQuantity = item.getQuantity() + cartItem.getQuantity();
                item.setQuantity(newQuantity);

                // Cập nhật số lượng sản phẩm trực tiếp lên Firebase
                String cartItemId = item.getCartItemId();
                cartRef.child(cartItemId).child("quantity").setValue(newQuantity)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(DetailActivity.this, "Đã cập nhật giỏ hàng", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DetailActivity.this, "Cập nhật giỏ hàng thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                isProductExists = true;
                break;
            }
        }
        // Nếu sản phẩm chưa tồn tại trong giỏ hàng, thêm mới
        if (!isProductExists) {
            // Thêm cartItem vào Firebase Realtime Database
            String cartItemId = cartRef.push().getKey(); // Tạo khóa ngẫu nhiên cho mỗi cart item
            cartItem.setCartItemId(cartItemId); // Thiết lập cartItemId của cartItem

            cartRef.child(cartItemId).setValue(cartItem)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(DetailActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DetailActivity.this, "Thêm vào giỏ hàng thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }
}