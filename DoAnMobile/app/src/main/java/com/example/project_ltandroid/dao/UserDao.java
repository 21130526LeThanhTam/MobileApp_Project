package com.example.project_ltandroid.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.project_ltandroid.SQLiteHelper.CreateDatabase;

public class UserDao {
    SQLiteDatabase database;

    public UserDao(Context context) {
        CreateDatabase createDatabase= new CreateDatabase(context);
        database= createDatabase.open();
    }

}
