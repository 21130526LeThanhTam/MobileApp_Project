package com.example.lab1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab1.R;
import com.example.lab1.adapter.OrderAdapterRecycleView;
import com.example.lab1.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class admin_qldonhang_fm extends Fragment {
    RecyclerView recyclerView;
    OrderAdapterRecycleView orderAdapterRecycleView;
    ProgressBar progressBar;
    List<String> stt=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_admin_fm_qldonhang,container,false);
        progressBar=view.findViewById(R.id.progressBar);
        recyclerView=view.findViewById(R.id.qldonhang_content);
        orderAdapterRecycleView= new OrderAdapterRecycleView(getOrderList(), getContext(),stt);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(orderAdapterRecycleView);
        return view;
    }

    private List<Order> getOrderList() {
        List<Order> orderList=new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Order");
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Order order=dataSnapshot.getValue(Order.class);
                    orderList.add(order);

                    stt.add(dataSnapshot.getKey());


                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return  orderList;
    }
}
