package com.example.lab1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lab1.R;
import com.example.lab1.model.CartItem;

import java.util.List;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private List<CartItem> cartItems;
    private OnQuantityChangeListener quantityChangeListener;

    public interface OnQuantityChangeListener {
        void onQuantityChanged();
    }

    public CartAdapter(Context context, List<CartItem> cartItems, OnQuantityChangeListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.quantityChangeListener = listener;
    }

    public void updateCartItems(List<CartItem> newCartItems) {
        this.cartItems = newCartItems;
        notifyDataSetChanged();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        }

        CartItem cartItem = cartItems.get(position);

        ImageView productImageView = convertView.findViewById(R.id.product_image);
        TextView productNameTextView = convertView.findViewById(R.id.product_name);
        TextView productPriceTextView = convertView.findViewById(R.id.product_price);
        TextView productQuantityTextView = convertView.findViewById(R.id.product_quantity);
        TextView subButton = convertView.findViewById(R.id.sub);
        TextView plusButton = convertView.findViewById(R.id.plus);

        Glide.with(context).load(cartItem.getImageUrl()).into(productImageView);
        productNameTextView.setText(cartItem.getProductName());
        productPriceTextView.setText(String.format("$%s", cartItem.getPrice()));
        productQuantityTextView.setText(String.valueOf(cartItem.getQuantity()));

        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = cartItem.getQuantity();
                if (quantity > 0) {
                    cartItem.setQuantity(quantity - 1);
                    if (cartItem.getQuantity() == 0) {
                        cartItems.remove(position);
                    }
                    productQuantityTextView.setText(String.valueOf(cartItem.getQuantity()));
                    if (quantityChangeListener != null) {
                        quantityChangeListener.onQuantityChanged();
                    }
                }
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = cartItem.getQuantity();
                cartItem.setQuantity(quantity + 1);
                productQuantityTextView.setText(String.valueOf(cartItem.getQuantity()));
                if (quantityChangeListener != null) {
                    quantityChangeListener.onQuantityChanged();
                }
            }
        });

        return convertView;
    }
}
