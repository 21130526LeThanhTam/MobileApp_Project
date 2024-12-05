package com.example.lab1.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lab1.Interface.ItemClickListener;

import com.example.lab1.activity.DetailActivity;
import com.example.lab1.model.NewProduct;
import com.example.lab1.R;


import java.text.DecimalFormat;
import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.MyViewHolder> {
    Context context;
    List<NewProduct> array;

    public DrinkAdapter(Context context, List<NewProduct> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_category, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NewProduct newProduct = array.get(position);
        holder.tensp.setText(newProduct.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.giasp.setText("Giá: " + decimalFormat.format(Double.parseDouble(newProduct.getPrice())) + "đ");
        holder.mota.setText(newProduct.getDescription());
        Glide.with(context).load(newProduct.getImage()).into(holder.hinhanh);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    // click
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("chitiet", newProduct);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tensp, giasp, mota;
        ImageView hinhanh;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tensp = itemView.findViewById(R.id.item_drink_name);
            giasp = itemView.findViewById(R.id.item_drink_price);
            mota = itemView.findViewById(R.id.item_drink_mota);
            hinhanh = itemView.findViewById(R.id.item_drink_image);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }


}
