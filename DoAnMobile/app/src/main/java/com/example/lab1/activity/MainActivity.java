package com.example.lab1.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.example.lab1.R;
import com.example.lab1.adapter.CateAdapter;
import com.example.lab1.adapter.MenuItemAdapter;
import com.example.lab1.adapter.ProductAdapter;
import com.example.lab1.model.Category;
import com.example.lab1.model.MenuItemLView;
import com.example.lab1.model.Product;
import com.example.lab1.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    ListView listViewManHinhChinh;
    DrawerLayout drawerLayout;
    // =============================Adapter====================
    CateAdapter cateAdapter;
    ProductAdapter productAdapter;
    MenuItemAdapter menuItemAdapter;
    //==============================List========================
    List<Category> listOfCategory;
    List<Product> lisOfProduct;
    List<MenuItemLView> listMenuMain;
    RecyclerView recyclerViewChonMon;
    RecyclerView recycleMainView;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    //=====================================
    ImageView avatar;
    TextView name;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Anhxa();
        ActionBar();
        ActionViewFlipper();
        getDataIntent();
//showUserInformation();
    }

    private void ActionViewFlipper(){
        List<String> mangquangcao=new ArrayList<>();
        mangquangcao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        mangquangcao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        mangquangcao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png");
        for(int i = 0;i<mangquangcao.size();i++){
            ImageView imageView =new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }

    private void ActionBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_custom_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toobarmanhinhchinh);
        viewFlipper=findViewById(R.id.viewLipper);
        listViewManHinhChinh=findViewById(R.id.listviewMain);
        drawerLayout=findViewById(R.id.drawerlayout);
        recyclerViewChonMon= findViewById(R.id.recyclerViewChonMon);
        recycleMainView =findViewById(R.id.recycleMainView);
        auth= FirebaseAuth.getInstance();
        firebaseUser= auth.getCurrentUser();

        avatar= findViewById(R.id.img_avatar);
        name= findViewById(R.id.tv_name);
        email= findViewById(R.id.tv_email);
//        if(firebaseUser != null){
//            name.setText(firebaseUser.ge);
//        }


        // Khởi tạo listCategory
        listOfCategory = new ArrayList<>();
        listOfCategory.add(new Category(1, "Thức uống", R.drawable.drink));
        listOfCategory.add(new Category(2, "Mì cay", R.drawable.micay));
        listOfCategory.add(new Category(3, "Gà rán", R.drawable.garan));
        listOfCategory.add(new Category(4, "Cơm trộn", R.drawable.comtron));
        listOfCategory.add(new Category(5, "Đồ ăn vặt", R.drawable.doanvat));
        listOfCategory.add(new Category(6, "Đồ ăn vặt", R.drawable.doanvat));
        listOfCategory.add(new Category(7, "Đồ ăn vặt", R.drawable.doanvat));
        listOfCategory.add(new Category(8, "Đồ ăn vặt", R.drawable.doanvat));
        listOfCategory.add(new Category(9, "Đồ ăn vặt", R.drawable.doanvat));
        listOfCategory.add(new Category(10, "Đồ ăn vặt", R.drawable.doanvat));
        // khởi tạo listProduct
        lisOfProduct = new ArrayList<>();
        lisOfProduct.add(new Product(1,"xiên bẩn",R.drawable.doanvat,10000));
        lisOfProduct.add(new Product(2,"bún cá",R.drawable.doanvat,25000));
        lisOfProduct.add(new Product(3,"bánh canh",R.drawable.doanvat,20000));
        lisOfProduct.add(new Product(4,"cơm tấm",R.drawable.doanvat,25000));
        lisOfProduct.add(new Product(5,"mì cay",R.drawable.doanvat,39000));
        lisOfProduct.add(new Product(6,"bún riêu",R.drawable.doanvat,25000));
        // Khởi tạo menu
        listMenuMain = new ArrayList<>();
        listMenuMain.add(new MenuItemLView(1,"Trang chủ",R.mipmap.ic_launcher));
        listMenuMain.add(new MenuItemLView(2,"Cửa hàng",R.mipmap.ic_launcher));
        listMenuMain.add(new MenuItemLView(3,"Cài đặt",R.mipmap.ic_launcher));
        listMenuMain.add(new MenuItemLView(4,"Giỏ hàng",R.mipmap.ic_launcher));


        //khơỉ tạo adapter cho category
        cateAdapter=new CateAdapter(listOfCategory);
        recyclerViewChonMon.setAdapter(cateAdapter);
        LinearLayoutManager layoutManager =new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        recyclerViewChonMon.setLayoutManager(layoutManager);
        // khởi tạo adapter cho product
        productAdapter=new ProductAdapter(getApplicationContext(),lisOfProduct);
        recycleMainView.setHasFixedSize(true);
        recycleMainView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        recycleMainView.setAdapter(productAdapter);
        menuItemAdapter = new MenuItemAdapter(getApplicationContext(),listMenuMain);
        listViewManHinhChinh.setAdapter(menuItemAdapter);

    }
    private void showUserInformation(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;

        }
        // Name, email address, and profile photo Url
        String namee = user.getDisplayName();
        String emaill = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();
if (namee==null){
    name.setVisibility(View.GONE);
}
else {
    name.setVisibility(View.VISIBLE);
    name.setText(namee);
}

        email.setText(emaill);
        Glide.with(this).load(photoUrl).error(R.drawable.user).into(avatar);
    }

    private List<User>onClickReadData() {
        List<User>userList= new ArrayList<>();
        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= firebaseDatabase.getReference();
        databaseReference.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    User user= snap.getValue(User.class);
                    userList.add(user);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return userList;
    }
    public void getDataIntent(){
String phonenum = getIntent().getStringExtra("phonenumber");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.miCart) {
            Toast.makeText(this, "Giỏ hàng", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.miProfile) {
            View view =findViewById(R.id.miProfile);
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.inflate(R.menu.context_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    int itemId = menuItem.getItemId();
                    if(itemId== R.id.edit_profile){
                        Toast.makeText(MainActivity.this, "Cập nhật thông tin", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (itemId== R.id.change_pass) {
                        Toast.makeText(MainActivity.this, "Đổi mật khẩu", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (itemId== R.id.logout) {
                        Toast.makeText(MainActivity.this, "Đăng xuất", Toast.LENGTH_SHORT).show();
                        auth.signOut();
                        Intent intent= new Intent(MainActivity.this,LoginActivity.class);
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
