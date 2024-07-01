package com.example.lab1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lab1.R;
import com.example.lab1.model.Order;

import java.util.List;

public class OrderAdapter extends BaseAdapter {
    private Context context;
    private List<Order> orders;

    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
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

        Order order = orders.get(position);

        TextView orderDateTextView = convertView.findViewById(R.id.orderDate);
        TextView orderTotalTextView = convertView.findViewById(R.id.orderTotal);
        TextView recipientNameTextView = convertView.findViewById(R.id.recipientName);
        TextView recipientPhoneTextView = convertView.findViewById(R.id.recipientPhone);
        TextView recipientAddressTextView = convertView.findViewById(R.id.recipientAddress);

        orderDateTextView.setText(order.getOrderDate());
        //orderTotalTextView.setText(String.format("$%.2f", order.getTotalPrice()));
        recipientNameTextView.setText(order.getRecipientName());
        recipientPhoneTextView.setText(order.getRecipientPhone());
        recipientAddressTextView.setText(order.getRecipientAddress());

        return convertView;
    }
}
