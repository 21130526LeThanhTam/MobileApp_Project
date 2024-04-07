package com.example.lab1.adapter;


import android.icu.util.ULocale;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab1.R;
import com.example.lab1.model.Category;

import java.util.List;
import java.util.zip.Inflater;

public class CateAdapter extends RecyclerView.Adapter<CateAdapter.MyViewHolder> {
    private List<Category> listCate;

    public CateAdapter(List<Category> listCate) {
        this.listCate = listCate;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category category= listCate.get(position);
        holder.imageView.setImageResource(category.getImage_category());
        holder.textView.setText(category.getName_category());
    }

    @Override
    public int getItemCount() {
        return listCate.size();
    }
    //for(int position=0;position);position<listCate.size();position++

    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            textView =itemView.findViewById(R.id.item_tenCate);

        }
    }

}
