package com.example.project_ltandroid.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.bumptech.glide.Glide;
import com.example.project_ltandroid.R;
import com.example.project_ltandroid.model.NewProduct;

import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {
    TextView tensp, giasp, mota;
    Button btnThem;
    ImageView hinhanh;
    Spinner spinner;
    Toolbar toolbar;


//sử dụng getIntent() để nhận Intent và từ đó lấy các dữ liệu đã được truyền qua từ MainActivity. Sau đó, bạn hiển thị các thông tin này lên giao diện (TextView cho tên, giá và mô tả sản phẩm, ImageView cho hình ảnh sản phẩm).

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
    }

    private void initData() {
        NewProduct newProduct = (NewProduct) getIntent().getSerializableExtra("chitiet");
        tensp.setText(newProduct.getName());
        mota.setText(newProduct.getDescription());
        Glide.with(getApplicationContext()).load(newProduct.getImage()).into(hinhanh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText("Giá: " + decimalFormat.format(Double.parseDouble(newProduct.getPrice())) + "đ");
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
}