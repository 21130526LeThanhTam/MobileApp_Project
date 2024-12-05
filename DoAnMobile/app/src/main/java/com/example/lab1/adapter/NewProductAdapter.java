package com.example.lab1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lab1.Interface.CategoryClickListener;
import com.example.lab1.model.NewProduct;
import com.example.lab1.R;

import java.text.DecimalFormat;
import java.util.List;

// adapter này dùng để load các sản phẩm từ db ở trang chủ
public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.MyViewHolder> {
    Context context;
    List<NewProduct> newProductList;
    private CategoryClickListener listener;


    public NewProductAdapter(Context context, List<NewProduct> newProductList, CategoryClickListener listener) {
        this.context = context;
        this.newProductList = newProductList;
        this.listener = listener;
    }

    // Phương thức này được thêm để cập nhật danh sách sản phẩm đã lọc
    public void filterList(List<NewProduct> filteredList) {
        newProductList = filteredList;
        notifyDataSetChanged();
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
        holder.price.setText("Giá: " + decimalFormat.format(Double.parseDouble(newProduct.getPrice())) + "đ");
        Glide.with(context).load(newProduct.getImage()).into(holder.imageView);

        // Set the click listener for the item
        holder.itemView.setOnClickListener(v -> listener.onClick(v, position, false));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onClick(v, position, true);
            return true;
        });
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
