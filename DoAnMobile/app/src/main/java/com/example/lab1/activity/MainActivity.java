package com.example.lab1.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lab1.R;
import com.example.lab1.adapter.CateAdapter;
import com.example.lab1.adapter.MenuItemAdapter;
import com.example.lab1.adapter.ProductAdapter;
import com.example.lab1.model.Category;
import com.example.lab1.model.MenuItemLView;
import com.example.lab1.model.Product;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Anhxa();
        ActionBar();
        ActionViewFlipper();

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



        // Khởi tạo listCategory
        listOfCategory = new ArrayList<>();
        listOfCategory.add(new Category(1, "Thức uống", R.drawable.drink));
        listOfCategory.add(new Category(2, "Mì cay", R.drawable.micay));
        listOfCategory.add(new Category(3, "Gà rán", R.drawable.garan));
        listOfCategory.add(new Category(4, "Cơm trộn", R.drawable.comtron));
        listOfCategory.add(new Category(5, "Đồ ăn vặt", R.drawable.doanvat));
        // khởi tạo listProduct
        lisOfProduct = new ArrayList<>();
        lisOfProduct.add(new Product(1,"xiên bẩn",10000,R.drawable.doanvat));
        lisOfProduct.add(new Product(2,"bún cá",25000,R.drawable.doanvat));
        lisOfProduct.add(new Product(3,"bánh canh",20000,R.drawable.doanvat));
        lisOfProduct.add(new Product(4,"cơm tấm",25000,R.drawable.doanvat));
        lisOfProduct.add(new Product(5,"mì cay",39000,R.drawable.doanvat));
        lisOfProduct.add(new Product(6,"bún riêu",25000,R.drawable.doanvat));
        // Khởi tạo menu
        listMenuMain = new ArrayList<>();
        listMenuMain.add(new MenuItemLView(1,"Trang chủ",R.mipmap.ic_launcher));
        listMenuMain.add(new MenuItemLView(2,"Cửa hàng",R.mipmap.ic_launcher));
        listMenuMain.add(new MenuItemLView(3,"Cài đặt",R.mipmap.ic_launcher));
        listMenuMain.add(new MenuItemLView(4,"Giỏ hàng",R.mipmap.ic_launcher));

        //khơỉ tạo adapter
        cateAdapter=new CateAdapter(listOfCategory);
        recyclerViewChonMon.setAdapter(cateAdapter);
        recyclerViewChonMon.setLayoutManager(new GridLayoutManager(this,listOfCategory.size()));
        // khởi tạo adapter cho product
//        productAdapter=new ProductAdapter(getApplicationContext(),lisOfProduct);
//        recycleMainView.setAdapter(productAdapter);
//        recycleMainView.setLayoutManager(new GridLayoutManager(this,2));
        // Khởi tạo adapter cho menuItem
        menuItemAdapter = new MenuItemAdapter(getApplicationContext(),listMenuMain);
        listViewManHinhChinh.setAdapter(menuItemAdapter);

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
                    if(id== R.id.edit_profile){
                        Toast.makeText(MainActivity.this, "Cập nhật thông tin", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (id== R.id.change_pass) {
                        Toast.makeText(MainActivity.this, "Đổi mật khẩu", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (id== R.id.logout) {
                        Toast.makeText(MainActivity.this, "Đăng xuất", Toast.LENGTH_SHORT).show();
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
