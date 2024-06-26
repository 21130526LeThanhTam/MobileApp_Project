package com.example.project_ltandroid.SQLiteHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CreateDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AppMobi";
    private static int DATABASE_VERSION = 1;

    public CreateDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "user_name VARCHAR(250)," +
                "password VARCHAR(250)," +
                "phone VARCHAR(250)," +
                "email VARCHAR(250)," +
                "hash VARCHAR(250)," +
                "picture REAL," +
                "create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(sql);

        // Bỏ chú thích và sửa phần này nếu bạn muốn chèn một người dùng khi tạo bảng
        // String sql1 = "INSERT INTO users (user_name, password, phone, email, hash, picture, create_at) " +
        //               "VALUES ('Le Thanh Tam', '123123', '0911281672', 'tamle7723@gmail.com', NULL, 1, '2024-04-04 15:52:48')";
        // db.execSQL(sql1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public SQLiteDatabase open() {
        return this.getWritableDatabase();
    }
}

