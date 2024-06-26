package com.example.project_ltandroid.activity;

import static java.util.Locale.filter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import androidx.core.view.GravityCompat;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_ltandroid.Interface.CategoryClickListener;
import com.example.project_ltandroid.Interface.ItemClickListener;
import com.example.project_ltandroid.R;
import com.example.project_ltandroid.adapter.CategoryAdapter;
import com.example.project_ltandroid.adapter.NewProductAdapter;
import com.example.project_ltandroid.adapter.SearchAdapter;
import com.example.project_ltandroid.adapter.ToolbarAdapter;
import com.example.project_ltandroid.model.Category;
import com.example.project_ltandroid.model.NewProduct;
import com.example.project_ltandroid.model.ToolbarModel;
import com.example.project_ltandroid.model.User;
import com.example.project_ltandroid.retrofit.ApiBanHang;
import com.example.project_ltandroid.retrofit.RetrofitClient;
import com.example.project_ltandroid.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    //=====================================
    ImageView avatar;
    TextView name;
    TextView email;

    // chức năng tìm kiếm sản phẩm
    EditText searchBox;
    RecyclerView searchRecyclerView;
    SearchAdapter searchAdapter;
    List<NewProduct> allProducts; // Danh sách tất cả sản phẩm
    List<NewProduct> filteredProducts = new ArrayList<>(); // Danh sách sản phẩm đã lọc

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Anhxa();
        ActionBar();
        getToolbar();
        getNewProduct();
        getEventClickToolbar();
        setupSearch();


        if (isConnect(this)) {
            Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
            ActionViewFlipper();
            getCategory();
        } else {
            Toast.makeText(getApplicationContext(), "khong co internet, vui long ket noi", Toast.LENGTH_LONG).show();
        }

        // Xử lý tìm kiếm


    }

    private void setupSearch() {
        searchAdapter = new SearchAdapter(this, filteredProducts, new CategoryClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if (!isLongClick) {
                    NewProduct product = filteredProducts.get(pos);
                    Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                    intent.putExtra("chitiet", product);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerView.setAdapter(searchAdapter);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterProducts(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void filterProducts(String query) {
        filteredProducts.clear(); //xóa hết các sản phẩm đã lọc trước đó trong danh sách
        if (query.isEmpty()) {
            searchRecyclerView.setVisibility(View.GONE);
        } else {
            for (NewProduct product : allProducts) {
                if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredProducts.add(product);
                }
            }
            searchAdapter.setFilteredList(filteredProducts);
            searchRecyclerView.setVisibility(View.VISIBLE);
        }
    }

//    private void getDataIntent() {
//      String phonenum = getIntent().getStringExtra("phonenumber");
//    }

    private void getEventClickToolbar() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                switch (i) {
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent setting = new Intent(getApplicationContext(), SettingActivity.class);
                        setting.putExtra("category_id", 1);
                        startActivity(setting);
                        break;
                    case 2:
                        Intent contact = new Intent(getApplicationContext(), ContactActivity.class);
                        startActivity(contact);
                        break;
                    case 3:
                        Intent information = new Intent(getApplicationContext(), InformationActivity.class);
                        startActivity(information);
                        break;

                }


            }
        });
    }

    private void getNewProduct() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("newProducts");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allProducts = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NewProduct product = snapshot.getValue(NewProduct.class);
                    allProducts.add(product);
                }
                // Khởi tạo adapter
                newProductAdapter = new NewProductAdapter(getApplicationContext(), allProducts, new CategoryClickListener() {
                    @Override
                    public void onClick(View view, int pos, boolean isLongClick) {
                        if (!isLongClick) {
                            NewProduct newProduct = allProducts.get(pos);
                            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                            intent.putExtra("chitiet", newProduct);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
                recyclerViewManHinhChinh.setAdapter(newProductAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Không kết nối được với server: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


//    private void getNewProduct() {
//        compositeDisposable.add(apiBanHang.getNewProduct()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        newProductModel -> {
//                            if (newProductModel.isSuccess()) {
//                                allProducts = newProductModel.getResult();
//                                // khởi tạo adapter
//                                newProductAdapter = new NewProductAdapter(getApplicationContext(), allProducts, new CategoryClickListener() {
//                                    @Override
//                                    public void onClick(View view, int pos, boolean isLongClick) {
//                                        if (!isLongClick) {
//                                            NewProduct newProduct = allProducts.get(pos);
//                                            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
//                                            intent.putExtra("chitiet", newProduct);
//                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                            startActivity(intent);
//                                        }
//                                    }
//                                });
//                                recyclerViewManHinhChinh.setAdapter(newProductAdapter);
//                            }
//                        }, throwable -> {
//                            Toast.makeText(getApplicationContext(), "Khong ket noi duoc voi server: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                )
//        );
//    }

//    private void getToolbar() {
//        compositeDisposable.add(apiBanHang.getToolbar()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        toolbarModel -> {
//                            if (toolbarModel.isSuccess()) {
//                                listToolbar = toolbarModel.getResult();
//                                // khởi tạo adapter
//                                toolbarAdapter = new ToolbarAdapter(getApplicationContext(), listToolbar);
//                                listViewManHinhChinh.setAdapter(toolbarAdapter);
//
//
//                            }
//                        }
//
//                )
//        );
//    }
private void getToolbar() {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("toolbar");

    myRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            listToolbar = new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                try {
                    com.example.project_ltandroid.model.Toolbar toolbar = snapshot.getValue(com.example.project_ltandroid.model.Toolbar.class);
                    listToolbar.add(toolbar);
                } catch (DatabaseException e) {
                    e.printStackTrace();
                    // Handle error
                }
            }
            // Khởi tạo adapter
            toolbarAdapter = new ToolbarAdapter(getApplicationContext(), listToolbar);
            listViewManHinhChinh.setAdapter(toolbarAdapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(), "Không kết nối được với server: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
        }
    });
}

//    private void getCategory() {
//        compositeDisposable.add(apiBanHang.getCategory()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        categoryModel -> {
//                            if (categoryModel.isSuccess()) {
//                                listCategory = categoryModel.getResult();
//                                // khởi tạo adapter với CategoryClickListener
//                                categoryAdapter = new CategoryAdapter(this, listCategory, new CategoryClickListener() {
//                                    @Override
//                                    public void onClick(View view, int pos, boolean isLongClick) {
//                                        if (!isLongClick) {
//                                            int categoryId = listCategory.get(pos).getId();
//                                            Toast.makeText(getApplicationContext(), String.valueOf(categoryId), Toast.LENGTH_SHORT).show();
//                                            Intent drinkIntent = new Intent(getApplicationContext(), CategoryActivity.class);
//                                            drinkIntent.putExtra("category_id", categoryId);
//                                            startActivity(drinkIntent);
//                                        }
//                                    }
//                                });
//                                recyclerViewCategory.setAdapter(categoryAdapter);
//                            }
//                        },
//                        throwable -> {
//                            Toast.makeText(getApplicationContext(), "Failed to load categories", Toast.LENGTH_SHORT).show();
//                        }
//                )
//        );
//    }
private void getCategory() {
    DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("categories");

   categoryRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            listCategory = new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                try {
                    Category category = snapshot.getValue(Category.class);
                    listCategory.add(category);
                } catch (DatabaseException e) {
                    e.printStackTrace();
                    // Handle error
                }
            }
            // Khởi tạo adapter với CategoryClickListener
            categoryAdapter = new CategoryAdapter(MainActivity.this, listCategory, new CategoryClickListener() {
                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if (!isLongClick) {
                        int categoryId = listCategory.get(pos).getId(); // Lấy id kiểu int
                        Toast.makeText(getApplicationContext(), String.valueOf(categoryId), Toast.LENGTH_SHORT).show();
                        Intent drinkIntent = new Intent(getApplicationContext(), CategoryActivity.class);
                        drinkIntent.putExtra("category_id", categoryId);
                        startActivity(drinkIntent);
                    }
                }
            });
            recyclerViewCategory.setAdapter(categoryAdapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(), "Không kết nối được với server: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
        }
    });
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

    private List<User> onClickReadData() {
        List<User> userList = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    User user = snap.getValue(User.class);
                    userList.add(user);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return userList;
    }

    private void Anhxa() {
        // User
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        avatar = findViewById(R.id.img_avatar);
        name = findViewById(R.id.tv_name);
        email = findViewById(R.id.tv_email);

        toolbar = findViewById(R.id.toobarmanhinhchinh);
        viewFlipper = findViewById(R.id.viewLipper);
        // tim kiem
        searchBox = findViewById(R.id.search_box);
        searchRecyclerView = findViewById(R.id.recyclerViewSearch);

        // Khởi tạo danh sách
        allProducts = new ArrayList<>();
        filteredProducts = new ArrayList<>();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.miCart) {
            Toast.makeText(this, "Giỏ hàng", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.miProfile) {
            View view = findViewById(R.id.miProfile);
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.inflate(R.menu.context_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    int itemId = menuItem.getItemId();
                    if (itemId == R.id.edit_profile) {
                        Toast.makeText(MainActivity.this, "Cập nhật thông tin", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (itemId == R.id.change_pass) {
                        Toast.makeText(MainActivity.this, "Đổi mật khẩu", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (itemId == R.id.logout) {
                        Toast.makeText(MainActivity.this, "Đăng xuất", Toast.LENGTH_SHORT).show();
                        auth.signOut();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        return true;
                    }
                    return false;
                }
            });
            popupMenu.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}