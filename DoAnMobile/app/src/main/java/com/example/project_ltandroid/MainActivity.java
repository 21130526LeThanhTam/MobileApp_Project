package com.example.project_ltandroid;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.view.GravityCompat;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_ltandroid.adapter.CategoryAdapter;
import com.example.project_ltandroid.adapter.NewProductAdapter;
import com.example.project_ltandroid.adapter.ToolbarAdapter;
import com.example.project_ltandroid.model.Category;
import com.example.project_ltandroid.model.NewProduct;
import com.example.project_ltandroid.retrofit.ApiBanHang;
import com.example.project_ltandroid.retrofit.RetrofitClient;
import com.example.project_ltandroid.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewManHinhChinh;
    ListView listViewManHinhChinh;
    RecyclerView recyclerViewCategory;

    ToolbarAdapter toolbarAdapter;
    List<com.example.project_ltandroid.model.Toolbar> listToolbar;

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    CategoryAdapter categoryAdapter;
    List<Category> listCategory;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<NewProduct> newProductModelList;
    NewProductAdapter newProductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Anhxa();
        ActionBar();
        getToolbar();
        getNewProduct();

        if (isConnect(this)) {
            Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
            ActionViewFlipper();
            getCategory();
        } else {
            Toast.makeText(getApplicationContext(), "khong co internet, vui long ket noi", Toast.LENGTH_LONG).show();
        }

    }

    private void getNewProduct() {
        compositeDisposable.add(apiBanHang.getNewProduct()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                newProductModel -> {
                            if(newProductModel.isSuccess()) {
                                newProductModelList = newProductModel.getResult();
                                // khởi tạo adapter
                                newProductAdapter = new NewProductAdapter(getApplicationContext(), newProductModelList);
                                recyclerViewManHinhChinh.setAdapter(newProductAdapter);

                            }
                                }, throwable -> {
                                    Toast.makeText(getApplicationContext(), "Khong ket noi duoc voi server" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                                }

                        )
        );
    }

    private void getToolbar() {
        compositeDisposable.add(apiBanHang.getToolbar()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        toolbarModel -> {
                            if (toolbarModel.isSuccess()) {
                                listToolbar = toolbarModel.getResult();
                                // khởi tạo adapter
                                toolbarAdapter = new ToolbarAdapter(getApplicationContext(), listToolbar);
                                listViewManHinhChinh.setAdapter(toolbarAdapter);


                            }
                        }

                )
        );
    }

    private void getCategory() {
        compositeDisposable.add(apiBanHang.getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categoryModel -> {
                            if (categoryModel.isSuccess()) {
                                listCategory = categoryModel.getResult();
                                // khởi tạo adapter
                                categoryAdapter = new CategoryAdapter(this, listCategory);
                                recyclerViewCategory.setAdapter(categoryAdapter);
                            }
                        }

                )
        );
    }

    // viewflipper chạy quảng cáo
    private void ActionViewFlipper() {
        List<String> mangquancao = new ArrayList<>();
        mangquancao.add("https://i.pinimg.com/originals/49/1e/e9/491ee929be5ce1c3eb05ff30ec6ed247.jpg");
        mangquancao.add("https://i.pinimg.com/736x/50/56/27/505627676ce628216441f5d20e8f2d0a.jpg");
        mangquancao.add("https://i.pinimg.com/originals/ac/c4/99/acc4999915b7e75da7b55ece26ed5c3b.jpg");
        for (int i = 0; i < mangquancao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquancao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);

    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toobarmanhinhchinh);
        viewFlipper = findViewById(R.id.viewLipper);

        // khung san pham moi
        recyclerViewManHinhChinh = findViewById(R.id.recycleMainView);
        RecyclerView.LayoutManager layoutNewProduct = new GridLayoutManager(this, 2);
        recyclerViewManHinhChinh.setLayoutManager(layoutNewProduct);
        recyclerViewManHinhChinh.setHasFixedSize(true);


        listViewManHinhChinh = findViewById(R.id.listviewMain);
        recyclerViewCategory = findViewById(R.id.listViewCategory);
        navigationView = findViewById(R.id.navigationview);
        drawerLayout = findViewById(R.id.drawerlayout);

        // khởi tạo list
        listCategory = new ArrayList<>();
        listToolbar = new ArrayList<>();
        newProductModelList = new ArrayList<>();

        // định dạng lại menu category
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategory.setLayoutManager(layoutManager);


    }

    private boolean isConnect(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // thêm quyền vào, nếu không sẽ bị lỗi
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || mobile != null && mobile.isConnected()) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}