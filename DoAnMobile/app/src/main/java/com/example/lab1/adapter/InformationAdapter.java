package com.example.lab1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lab1.R;
import com.example.lab1.model.Information;

import java.util.List;

public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.InformationViewHolder> {
    List<Information> informationList;
    Context context;

    public InformationAdapter(List<Information> informationList, Context context) {
        this.informationList = informationList;
        this.context = context;
    }

    @NonNull
    @Override
    public InformationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_information_item,parent,false);
    return new InformationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InformationAdapter.InformationViewHolder holder, int position) {
        Information information=informationList.get(position);
        Glide.with(context).load(information.getAvt()).into(holder.avt);
        holder.mssv.setText(information.getMssv());
        holder.name.setText(information.getName());

    }

    @Override
    public int getItemCount() {
        return informationList.size();
    }

    public class InformationViewHolder extends RecyclerView.ViewHolder{
        ImageView avt;
        TextView mssv, name;

        public InformationViewHolder(@NonNull View itemView) {
            super(itemView);
           avt=itemView.findViewById(R.id.information_item_avt);
            mssv=itemView.findViewById(R.id.information_item_mssv);
            name=itemView.findViewById(R.id.information_item_name);
        }
    }
}
