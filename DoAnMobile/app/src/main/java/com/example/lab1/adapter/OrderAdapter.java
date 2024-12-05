package com.example.lab1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab1.R;
import com.example.lab1.model.Order;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OrderAdapter extends BaseAdapter {

    private Context context;
    private List<Order> orderList;
    private DatabaseReference ordersRef;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
        this.ordersRef = FirebaseDatabase.getInstance().getReference().child("Order");
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

        TextView orderDateTextView = convertView.findViewById(R.id.orderDate);
        TextView orderTotalTextView = convertView.findViewById(R.id.orderTotal);
        TextView recipientNameTextView = convertView.findViewById(R.id.recipientName);
        TextView recipientPhoneTextView = convertView.findViewById(R.id.recipientPhone);
        TextView recipientAddressTextView = convertView.findViewById(R.id.recipientAddress);
        TextView orderStatusTextView = convertView.findViewById(R.id.orderStatus);
        Button cancelOrderButton = convertView.findViewById(R.id.cancelOrderButton);

        orderDateTextView.setText(order.getOrderDate());
        orderTotalTextView.setText(String.format("$%.2f", order.getTotalPrice()));

        recipientNameTextView.setText(order.getRecipientName());
        recipientPhoneTextView.setText(order.getRecipientPhone());
        recipientAddressTextView.setText(order.getRecipientAddress());
        orderStatusTextView.setText(order.getOrderStatus());
        // Xử lý logic cho nút cancelOrderButton
        if (order.getOrderStatus().equals("Đang xử lý")) {
            cancelOrderButton.setText("Hủy");
            cancelOrderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Cập nhật trạng thái đơn hàng thành "Đã hủy"
                    orderStatusTextView.setText("Đã hủy");
                    order.setOrderStatus("Đã hủy");
                    ordersRef.child(order.getOrderId()).child("orderStatus").setValue("Đã hủy")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Ẩn nút cancelOrderButton sau khi hủy đơn hàng thành công
                                    cancelOrderButton.setVisibility(View.GONE);
                                    Toast.makeText(context, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Không thể hủy đơn hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        } else if (order.getOrderStatus().equals("Đang giao hàng")) {
            cancelOrderButton.setText("Đã nhận");
            cancelOrderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Cập nhật trạng thái đơn hàng thành "Giao thành công"
                    orderStatusTextView.setText("Giao thành công");
                    order.setOrderStatus("Giao thành công");
                    ordersRef.child(order.getOrderId()).child("orderStatus").setValue("Giao thành công")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Ẩn nút cancelOrderButton sau khi giao thành công
                                    cancelOrderButton.setVisibility(View.GONE);
                                    Toast.makeText(context, "Đã giao thành công đơn hàng", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Không thể cập nhật trạng thái đơn hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        } else {
            // Ẩn nút cancelOrderButton khi trạng thái đơn hàng không phù hợp
            cancelOrderButton.setVisibility(View.GONE);
        }


        return convertView;
    }
}