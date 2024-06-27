package com.example.lab1.model;

public class Information {
    String avt;
    String mssv;
    String name;

    public Information() {
    }
    public Information(String avt, String mssv, String name) {
        this.avt = avt;
        this.mssv = mssv;
        this.name = name;
    }

    public String getAvt() {
        return avt;
    }

    public void setAvt(String avt) {
        this.avt = avt;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}