package com.example.lab1.model;

public class MenuItemLView {
    private int id;
    private String name_menu;
    private int image_menu;

    public MenuItemLView(int id, String name_menu, int image_menu) {
        this.id = id;
        this.name_menu = name_menu;
        this.image_menu = image_menu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_menu() {
        return name_menu;
    }

    public void setName_menu(String name_menu) {
        this.name_menu = name_menu;
    }

    public int getImage_menu() {
        return image_menu;
    }

    public void setImage_menu(int image_menu) {
        this.image_menu = image_menu;
    }
}
