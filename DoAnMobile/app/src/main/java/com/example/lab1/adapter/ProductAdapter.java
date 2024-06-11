package com.example.lab1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab1.R;
import com.example.lab1.model.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder>{
    Context context;// màn hình muốn vẽ.
    List<Product> productList ;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_show,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {
        Product product=productList.get(position);
//        holder.imageView.setImageResource(product.getImage_product());
        DecimalFormat decimalFormat= new DecimalFormat("###,###,###");
        holder.productName.setText(product.getName_product());
        holder.price.setText("Giá : "+decimalFormat.format(product.getPrice())+"Đ");
        Picasso.get().load(product.getImage_product())
                .placeholder(R.drawable.logo)
                .error(R.drawable.micay)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public  TextView productName;
        public TextView price;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageProduct);
            productName = (TextView) itemView.findViewById(R.id.textViewProductName);
            price = (TextView) itemView.findViewById(R.id.textViewProductPrice);
        }
    }

}
