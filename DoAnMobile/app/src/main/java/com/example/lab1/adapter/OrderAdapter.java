package com.example.lab1.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lab1.R;
import com.example.lab1.model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> implements ListAdapter {

    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.orderDate.setText(order.getOrderDate());
        holder.orderStatus.setText(order.getOrderStatus());
        holder.recipientName.setText(order.getRecipientName());
        holder.recipientPhone.setText(order.getRecipientPhone());
        holder.recipientAddress.setText(order.getRecipientAddress());
        holder.totalPrice.setText(String.valueOf(order.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView orderDate, orderStatus, recipientName, recipientPhone, recipientAddress, totalPrice;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.orderDate);
//            orderStatus = itemView.findViewById(R.id.orderStatus);
            recipientName = itemView.findViewById(R.id.recipientName);
            recipientPhone = itemView.findViewById(R.id.recipientPhone);
            recipientAddress = itemView.findViewById(R.id.recipientAddress);
            totalPrice = itemView.findViewById(R.id.orderTotal);
        }
    }
}
