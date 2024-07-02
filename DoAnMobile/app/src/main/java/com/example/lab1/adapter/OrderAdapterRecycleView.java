package com.example.lab1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab1.R;
import com.example.lab1.model.Order;
import com.example.lab1.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapterRecycleView extends RecyclerView.Adapter<OrderAdapterRecycleView.OrderViewHolder> {
    List<Order> orderList = new ArrayList<>();
    Context context;
    List<String> stt;
    int a=1;
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
        holder.stt.setText(String.valueOf(a));
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(order.getUserId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    holder.tenKH.setText(String.valueOf(user.getName()));
                    holder.tenKH.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu popupMenu = new PopupMenu(context,v);
                            popupMenu.getMenu().clear();
                            popupMenu.getMenu().add(0,1,0,"User ID: "+user.getId());
                            popupMenu.getMenu().add(0,2,0,"Email: "+user.getEmail());
                            popupMenu.getMenu().add(0,3,0,"Số điện thoại: "+user.getPhone());
                            popupMenu.show();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,v);
                popupMenu.getMenu().clear();
                DatabaseReference databaseReference1 =FirebaseDatabase.getInstance().getReference("newProducts");

                for(int i=0;i<order.getProducts().size();i++){
                    popupMenu.getMenu().add(0,i+1,0,"Sản phẩm "+(i+1)+": "+order.getProducts().get(i).toString());
                };
                popupMenu.show();
            }
        });
        holder.ngaydat.setText(String.valueOf(order.getOrderDate()));
        holder.ngaydat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,v);
                popupMenu.getMenu().clear();
                popupMenu.getMenu().add(0,1,0,"Người nhận: "+order.getRecipientName());
                popupMenu.getMenu().add(0,2,0,"Số điện thoại người nhận: "+order.getRecipientPhone());
                popupMenu.getMenu().add(0,2,0,"Địa chỉ người nhận: "+order.getRecipientAddress());
                popupMenu.show();
            }
        });
        holder.status.setText(String.valueOf(order.getOrderStatus()));
       int keyOfOrder=position;
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        changeOrderStatus("remove",order,stt.get(keyOfOrder),keyOfOrder);
                }

        });

        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeOrderStatus("check",order,stt.get(keyOfOrder),keyOfOrder);
            }
        });

    a++;
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
            if(order.getOrderStatus().equals("Đang xử lý")){
                order.setOrderStatus("Đã hủy");
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
               case "Đang xử lý":
                   order.setOrderStatus("Đang giao hàng");
                   databaseReference.child(stt).child("orderStatus").setValue("Đang giao hàng");
                   notifyItemChanged(position);
                   Toast.makeText(context,"Successful",Toast.LENGTH_SHORT).show();
                   break;
               default:
                   Toast.makeText(context,"Thao tác không được thực hiện ",Toast.LENGTH_SHORT).show();
                   break;
           }
        default:
            break;

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
