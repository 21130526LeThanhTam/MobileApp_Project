package com.example.lab1.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lab1.R;
import com.example.lab1.model.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OrderAdapter extends BaseAdapter {

    private Context context;
    private List<Order> orderList;
    private List<String> k;

    public OrderAdapter(Context context, List<Order> orderList, List<String> k) {
        this.context = context;
        this.orderList = orderList;
        this.k = k;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        }

        Order order = orderList.get(position);
//        String key = k.get(position);

        TextView orderDateTextView = convertView.findViewById(R.id.orderDate);
        TextView orderTotalTextView = convertView.findViewById(R.id.orderTotal);
        TextView recipientNameTextView = convertView.findViewById(R.id.recipientName);
        TextView recipientPhoneTextView = convertView.findViewById(R.id.recipientPhone);
        TextView recipientAddressTextView = convertView.findViewById(R.id.recipientAddress);
        TextView orderStatusTextView = convertView.findViewById(R.id.orderStatus);
        Button orderCancel = convertView.findViewById(R.id.cancelOrderButton);

        orderDateTextView.setText(order.getOrderDate());
        orderTotalTextView.setText(String.format("$%.2f", order.getTotalPrice()));
        orderStatusTextView.setText(order.getOrderStatus());
//        if(order.getOrderStatus().equals("Đang xử lí")){
//            orderCancel.setText("hủy");
//            orderCancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    order.setOrderStatus("Đã hủy");
//                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//                    DatabaseReference ordersRef = firebaseDatabase.getReference("Order").child(key);
//                    ordersRef.child("orderStatus").setValue("Đã hủy");
//                }
//            });
//        }
        recipientNameTextView.setText(order.getRecipientName());
        recipientPhoneTextView.setText(order.getRecipientPhone());
        recipientAddressTextView.setText(order.getRecipientAddress());

        return convertView;
    }
}