package com.example.lab1.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab1.R;
import com.example.lab1.model.CartItem;
import com.example.lab1.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class test extends AppCompatActivity {
    TextView view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        view= findViewById(R.id.aaaaaaaaaaaaaa);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Order");
       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               List<Order> a = new ArrayList<>();
               for (DataSnapshot dataSnapshot :snapshot.getChildren()){
               Order order=dataSnapshot.getValue(Order.class);
               view.setText(dataSnapshot.getKey());
               a.add(order);
               }

           }



           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
       List<CartItem>a = new ArrayList<>();
       a.add(new CartItem("a",1));
       Order order = new Order(a,"127","123","123","123","123","123");
       databaseReference.push().setValue(order);

    }

}
