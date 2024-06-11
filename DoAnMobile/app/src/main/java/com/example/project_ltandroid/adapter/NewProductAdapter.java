package com.example.project_ltandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_ltandroid.R;
import com.example.project_ltandroid.model.NewProduct;

import java.text.DecimalFormat;
import java.util.List;

public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.MyViewHolder> {
    Context context;
    List<NewProduct> newProductList;


    public NewProductAdapter(Context context, List<NewProduct> newProductList) {
        this.context = context;
        this.newProductList = newProductList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);


        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NewProduct newProduct = newProductList.get(position);
        holder.nameProduct.setText(newProduct.getName());
        // định dạng số tiền
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.price.setText("Giá: " + decimalFormat.format(Double.parseDouble(newProduct.getPrice())) + "Đ");
        Glide.with(context).load(newProduct.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return newProductList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nameProduct, price;
        ImageView imageView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameProduct = itemView.findViewById(R.id.itemproduct_name);
            price = itemView.findViewById(R.id.itemproduct_price);
            imageView = itemView.findViewById(R.id.itemproduct_image);
        }
    }
}
