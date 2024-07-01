package com.example.lab1.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.lab1.R;
import com.example.lab1.fragment.admin_qldonhang_fm;
import com.example.lab1.fragment.admin_qluser_fm;
import com.example.lab1.fragment.admin_trangchu_fm;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView menuAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        menuAdmin=findViewById(R.id.menuAdmin);
        menuAdmin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.menuAdmin:
               showMenuAdmin(view);
                break;

        }
    }
    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Thông báo")
                .setMessage("Bạn có muốn đăng xuất ?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }
    public void showMenuAdmin(View view){
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.inflate(R.menu.menu_admin);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction =fragmentManager.beginTransaction();
                Fragment fragment;
                switch (item.getItemId()){
                    case R.id.tv_homei:
                fragment = new admin_trangchu_fm();
                transaction.add(R.id.frame_admin,fragment);
                        transaction.commit();
                return true;
                case R.id.tv_qldonhang:
                fragment = new admin_qldonhang_fm();
                transaction.add(R.id.frame_admin,fragment);
                    transaction.commit();
                    return true;
                    case R.id.tv_qluser:
                        fragment = new admin_qluser_fm();

                        transaction.replace(R.id.frame_admin,fragment);
                        transaction.commit();
                        return true;
                    case R.id.btn_logout:
                        showLogoutConfirmationDialog();
                        return true;
                }

                return false;
            }
        });
popupMenu.show();
    }
}