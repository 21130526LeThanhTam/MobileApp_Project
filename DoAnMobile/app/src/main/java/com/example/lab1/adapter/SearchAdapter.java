package com.example.lab1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lab1.Interface.CategoryClickListener;
import com.example.lab1.model.NewProduct;
import com.example.lab1.R;

import java.text.DecimalFormat;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context context;
    private List<NewProduct> productList;
    private CategoryClickListener categoryClickListener;

    public SearchAdapter(Context context, List<NewProduct> productList, CategoryClickListener categoryClickListener) {
        this.context = context;
        this.productList = productList;
        this.categoryClickListener = categoryClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewProduct product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productDesciption.setText(product.getDescription());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.productPrice.setText("Giá: " + decimalFormat.format(Double.parseDouble(product.getPrice())) + "đ");
        Glide.with(context).load(product.getImage()).into(holder.productImage);
        holder.itemView.setOnClickListener(v -> categoryClickListener.onClick(v, position, false));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setFilteredList(List<NewProduct> filteredList) {
        this.productList = filteredList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productDesciption;
        ImageView productImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productDesciption = itemView.findViewById(R.id.product_mota);
            productImage = itemView.findViewById(R.id.product_image);
        }
    }
}
