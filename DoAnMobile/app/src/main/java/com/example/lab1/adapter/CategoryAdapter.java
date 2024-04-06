package com.example.lab1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lab1.R;
import com.example.lab1.model.Category;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    List<Category> arrayCate;
    Context context;

    public CategoryAdapter(List<Category> arrayCate, Context context) {
        this.arrayCate = arrayCate;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayCate.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder  {
        TextView name_category;
        ImageView image_category;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_category, null);
            viewHolder.name_category = view.findViewById(R.id.item_tenCate);
            viewHolder.image_category = view.findViewById(R.id.item_image);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.name_category.setText(arrayCate.get(i).getName_category());
        Glide.with(context).load(arrayCate.get(i).getImage_category()).into(viewHolder.image_category);

        return view;
    }
}
