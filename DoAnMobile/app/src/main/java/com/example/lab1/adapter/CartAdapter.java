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
import com.example.lab1.model.CartItem;

import java.util.List;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<CartItem> cartItems;

    public CartAdapter(Context context, int layout, List<CartItem> cartItems) {
        this.context = context;
        this.layout = layout;
        this.cartItems = cartItems;
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
        }

        TextView productNameTextView = convertView.findViewById(R.id.product_name);
        TextView productPriceTextView = convertView.findViewById(R.id.product_price);
        TextView productQuantityTextView = convertView.findViewById(R.id.product_quantity);
        ImageView productImageView = convertView.findViewById(R.id.product_image);

        CartItem cartItem = cartItems.get(position);

        productNameTextView.setText(cartItem.getProductName());
        productPriceTextView.setText(String.format("$%.2f", cartItem.getPrice()));
        productQuantityTextView.setText(String.valueOf(cartItem.getQuantity()));
        Glide.with(context).load(cartItem.getImageUrl()).into(productImageView);

        return convertView;
    }
}
