package com.example.project_ltandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project_ltandroid.Interface.CategoryClickListener;
import com.example.project_ltandroid.R;
import com.example.project_ltandroid.model.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private List<Category> listCate;
    private Context context;
    private CategoryClickListener listener;


    public CategoryAdapter(Context context, List<Category> listCate, CategoryClickListener listener) {
        this.context = context;
        this.listCate = listCate;
        this.listener = listener;
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
        holder.textView.setText(category.getName());
        Glide.with(context).load(category.getImage()).into(holder.imageView);

        // Set the click listener for the item
        holder.itemView.setOnClickListener(v -> listener.onClick(v, position, false));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onClick(v, position, true);
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return listCate.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            textView = itemView.findViewById(R.id.item_name);

        }


    }

}

