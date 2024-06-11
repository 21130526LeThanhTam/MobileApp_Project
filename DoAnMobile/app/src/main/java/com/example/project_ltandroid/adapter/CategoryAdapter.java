package com.example.project_ltandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project_ltandroid.R;
import com.example.project_ltandroid.model.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private List<Category> listCate;
    private Context context;

    public CategoryAdapter(Context context, List<Category> listCate) {
        this.context = context;
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
        holder.textView.setText(category.getName());
        Glide.with(context).load(category.getImage()).into(holder.imageView);
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
            textView =itemView.findViewById(R.id.item_name);

        }
    }

}

