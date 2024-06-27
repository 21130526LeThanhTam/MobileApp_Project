package com.example.lab1.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab1.R;
import com.example.lab1.adapter.InformationAdapter;
import com.example.lab1.model.Information;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class InformationActivity extends AppCompatActivity {
    private static final String TAG = InformationActivity.class.getSimpleName();
RecyclerView recyclerView;
InformationAdapter informationAdapter;
List<Information> informationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_information);
        recyclerView=findViewById(R.id.information_content);
        informationAdapter=new InformationAdapter(getInformation(),this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(informationAdapter);


    }

    public List<Information> getInformation(){
        List<Information> result=new ArrayList<>();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("About");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Information information=dataSnapshot.getValue(Information.class);
                    Log.d("ABoutttttttttttt",information.toString());
                    result.add(information);
                }
                informationAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage());
                System.out.println("lỗi");
            }
        });
        return result;
    }


    public static void main(String[] args) {
        List<Information> information ;
        InformationActivity informationActivity = new InformationActivity();
        information=informationActivity.getInformation();
        Log.d("a",information.toString());
    }
}