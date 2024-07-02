package com.example.lab1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lab1.R;
import com.example.lab1.model.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<CartItem> cartItems;
    private DatabaseReference cartRef;

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
        TextView subTextView = convertView.findViewById(R.id.sub);
        TextView plusTextView = convertView.findViewById(R.id.plus);
        ImageView productImageView = convertView.findViewById(R.id.product_image);

        CartItem cartItem = cartItems.get(position);

        productNameTextView.setText(cartItem.getProductName());
        productPriceTextView.setText(String.format("$%.2f", cartItem.getPrice()));
        productQuantityTextView.setText(String.valueOf(cartItem.getQuantity()));
        Glide.with(context).load(cartItem.getImageUrl()).into(productImageView);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String userId = currentUser.getUid();
        cartRef = firebaseDatabase.getReference("User").child(userId).child("cart");
        String cartItemId = cartItem.getCartItemId();
        subTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = cartItem.getQuantity() - 1;
                if (newQuantity > 0) {
                    cartItem.setQuantity(newQuantity);
                    productQuantityTextView.setText(String.valueOf(newQuantity));

                    // Cập nhật số lượng sản phẩm trực tiếp lên Firebase

                    cartRef.child(cartItemId).child("quantity").setValue(newQuantity);
                }else if (newQuantity == 0){
                    cartItems.remove(position);
                    cartRef.child(cartItemId).removeValue();
                    notifyDataSetChanged();
                }else{
                    Toast.makeText(context, "Số lượng sản phẩm không thể nhỏ hơn 1", Toast.LENGTH_SHORT).show();
                }
            }
        });
        plusTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = cartItem.getQuantity() + 1;
                cartItem.setQuantity(newQuantity);
                productQuantityTextView.setText(String.valueOf(newQuantity));

                // Cập nhật số lượng sản phẩm trực tiếp lên Firebase
                cartRef.child(cartItemId).child("quantity").setValue(newQuantity);
            }
        });

        return convertView;
    }
}
