package com.example.lab1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab1.R;
import com.example.lab1.model.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapterRecycleView extends RecyclerView.Adapter<OrderAdapterRecycleView.OrderViewHolder> {
    List<Order> orderList = new ArrayList<>();
    Context context;
    List<String> stt;
    public OrderAdapterRecycleView(List<Order> orderList, Context context, List<String> stt) {
        this.orderList = orderList;
        this.context = context;
        this.stt=stt;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_qldonhang_content,parent,false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
       Order  order = orderList.get(position);
        holder.stt.setText(String.valueOf(stt.get(position)));
        holder.tenKH.setText(String.valueOf(order.getUserId()));
        holder.ngaydat.setText(String.valueOf(order.getOrderDate()));
        holder.status.setText(String.valueOf(order.getOrderStatus()));
        holder.stt.setText(String.valueOf(stt));
       int keyOfOrder=position;
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        changeOrderStatus("remove",order,stt.get(keyOfOrder),keyOfOrder);
                        Toast.makeText(context,String.valueOf(order.getOrderStatus()), Toast.LENGTH_SHORT).show();

                }

        });

        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeOrderStatus("check",order,stt.get(keyOfOrder),keyOfOrder);

            }
        });


    }

    @Override
    public int getItemCount() {
        if(orderList==null ){
            return 0;
        }
        return orderList.size();
    }


   public void changeOrderStatus(String action,Order order,String stt,int position){
       DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Order");
        databaseReference.child(stt);
    switch(action){
        case "remove":
            Toast.makeText(context, order.getOrderStatus(), Toast.LENGTH_SHORT).show();
            if(order.getOrderStatus().equals("Đang xử lí")){
                order.setOderStatus("Đã hủy");
                databaseReference.child(stt).child("orderStatus").setValue("Đã hủy");
                notifyItemChanged(position);
                Toast.makeText(context, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show();
                break;
            }else{
                Toast.makeText(context,"Không thể hủy đơn hàng "+order.getOrderStatus(),Toast.LENGTH_SHORT).show();
                break;
            }

        case "check":
           switch (order.getOrderStatus()){
               case "Đang xử lí":
                   order.setOderStatus("Đang giao hàng");
                   databaseReference.child(stt).child("orderStatus").setValue("Đang giao hàng");
                   notifyItemChanged(position);

                   Toast.makeText(context,"Successful",Toast.LENGTH_SHORT).show();
                   break;
               default:
                   Toast.makeText(context,"Thao tác không được thực hiện ",Toast.LENGTH_SHORT).show();
                   break;
           }

    }

   };


    public class OrderViewHolder extends RecyclerView.ViewHolder  {
        TextView stt,tenKH,ngaydat,status;
        ImageView sp,remove,check;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            stt = itemView.findViewById(R.id.stt);
            tenKH = itemView.findViewById(R.id.tenKH);
            ngaydat = itemView.findViewById(R.id.ngaydat);
            status = itemView.findViewById(R.id.status);
            sp = itemView.findViewById(R.id.sp);
            remove = itemView.findViewById(R.id.remove);
            check = itemView.findViewById(R.id.check);
        }


    }

    public static void main(String[] args) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Order");
        String key =databaseReference.getKey();
        Log.e("TAGggggggggggggggggggggggggggg", "main: "+key);
    }
}
