package com.example.lab1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lab1.R;

import com.example.lab1.model.MenuItemLView;

import java.util.List;

public class MenuItemAdapter extends BaseAdapter {
    List<MenuItemLView> menuSroll;
    Context context;

    public MenuItemAdapter(Context context, List<MenuItemLView> menuSroll) {
        this.menuSroll = menuSroll;
        this.context = context;
    }


    @Override
    public int getCount() {
        return menuSroll.size();
    }

    @Override
    public Object getItem(int position) {
        return menuSroll.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder{
        TextView  textViewMenuItem;
        ImageView imageMenuItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            //View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.menu_scrollbar, parent,false);
            viewHolder.textViewMenuItem = convertView.findViewById(R.id.textViewMenuItem);
            viewHolder.imageMenuItem = convertView.findViewById(R.id.imageMenuItem);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.textViewMenuItem.setText(menuSroll.get(position).getName_menu());
        Glide.with(context).load(menuSroll.get(position).getImage_menu()).into(viewHolder.imageMenuItem);
        return convertView;
    }
}
