package com.example.lab1.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.lab1.SQLiteHelper.CreateDatabase;

public class UserDao {
    SQLiteDatabase database;

    public UserDao(Context context) {
        CreateDatabase createDatabase= new CreateDatabase(context);
        database= createDatabase.open();
    }

}
