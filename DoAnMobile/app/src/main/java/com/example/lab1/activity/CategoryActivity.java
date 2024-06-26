package com.example.lab1.activity;


import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab1.adapter.DrinkAdapter;
import com.example.lab1.model.NewProduct;
import com.example.lab1.retrofit.ApiBanHang;
import com.example.lab1.retrofit.RetrofitClient;
import com.example.lab1.utils.Utils;

import com.example.lab1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class CategoryActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    int category;
    DrinkAdapter drinkAdapter;
    List<NewProduct> productList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        // Retrieve the category ID from the intent
        category = getIntent().getIntExtra("category_id", 1);  // Default to category 1 if not provided

        Anhxa();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getData(category);
        getCategoryName(category - 1); // Lấy tên loại sản phẩm
        // Vì db trong realtime bị lệch 1 con số để trỏ đến đúng địa chỉ dữ liệu trong Firebase nên phải - 1
    }

    private void getCategoryName(int categoryId) {
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("categories").child(String.valueOf(categoryId));

        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String categoryName = dataSnapshot.child("name").getValue(String.class);
                    if (categoryName != null) {
                        // Hiển thị tên loại sản phẩm trên Toolbar
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setTitle(categoryName);
                        }
                        Toast.makeText(getApplicationContext(), categoryName, Toast.LENGTH_SHORT).show();
                        Log.d("getCate", "Category Name: " + categoryName + " id: " + categoryId);
                    } else {
                        Log.d("getCate", "Category name not found");
                    }
                } else {
                    Log.d("getCate", "Category not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("getCate", "Failed to read category.", databaseError.toException());
                Toast.makeText(getApplicationContext(), "Failed to read category: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




//    private void getCategoryName() {
//        compositeDisposable.add(apiBanHang.getCategoryName(category)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        categoryNameModel -> {
//                            Log.d("getCate", "Data received: " + new Gson().toJson(categoryNameModel));
//                            if (categoryNameModel.isSuccess()) {
//                                String categoryName = categoryNameModel.getCategoryName();
//                                Log.d("getCate", "Category Name: " + categoryName); // Kiểm tra giá trị categoryName
//                                // Hiển thị tên loại sản phẩm trên Toolbar
//                                if (getSupportActionBar() != null) {
//                                    getSupportActionBar().setTitle(categoryName);
//                                    Toast.makeText(getApplicationContext(), String.valueOf(categoryName), Toast.LENGTH_SHORT).show();
//                                    Log.d("getCate", "Data received successfully");
//                                }
//                            } else {
//                                Log.d("getCate", "API request unsuccessful: " + categoryNameModel.getMessage());
//                            }
//                        },
//                        throwable -> {
//                            // Xảy ra lỗi khi thực hiện request
//                            Log.e("getCate", "Error: " + throwable.getMessage());
//                            Toast.makeText(getApplicationContext(), "Lỗi kết nối: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                ));
//    }


//    private void getData() {
//        compositeDisposable.add(apiBanHang.getProduct(page, category)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        newProductModel -> {
//                            if (newProductModel.isSuccess()) {
//                                productList = newProductModel.getResult();
//                                // Kiểm tra dữ liệu đã về thành công
//
//                                // Dữ liệu đã về thành công
//                                Log.d("getData", "Data received successfully");
//                                Log.d("getData", "Data received: " + new Gson().toJson(newProductModel));
//                                // Tiếp tục xử lý dữ liệu ở đây
//                                drinkAdapter = new DrinkAdapter(this, productList);
//                                recyclerView.setAdapter(drinkAdapter);
//                            } else {
//                                // API không thành công, xử lý tại đây nếu cần
//                                Log.d("getData", "API request unsuccessful");
//                                Toast.makeText(getApplicationContext(), "Chưa có sản phẩm", Toast.LENGTH_SHORT).show();
//                            }
//                        },
//                        throwable -> {
//                            // Xảy ra lỗi khi thực hiện request
//                            Log.e("getData", "Error: " + throwable.getMessage());
//                            Toast.makeText(getApplicationContext(), "Lỗi kết nối: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                ));
//    }
private void getData(int category) {
    DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("newProducts");

    // Lọc sản phẩm theo category
    Query query = productsRef.orderByChild("category_id").equalTo(category);

    query.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<NewProduct> productList = new ArrayList<>();

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                NewProduct product = snapshot.getValue(NewProduct.class);
                if (product != null) {
                    productList.add(product);
                }
            }

            // Update RecyclerView or any other UI components here
            // For example: Initialize adapter and update RecyclerView
            drinkAdapter = new DrinkAdapter(getApplicationContext(), productList);
            recyclerView.setAdapter(drinkAdapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e("getDataForCategory", "Failed to read value.", databaseError.toException());
            Toast.makeText(getApplicationContext(), "Failed to read products for category from Firebase.", Toast.LENGTH_SHORT).show();
        }
    });
}


    private void Anhxa() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycle_getCategory);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        productList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
