package com.example.project_ltandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project_ltandroid.R;
import com.example.project_ltandroid.model.Toolbar;

import java.util.List;

public class ToolbarAdapter extends BaseAdapter {
    List<Toolbar> toolbars;
    Context context;

    public ToolbarAdapter(Context context, List<Toolbar> toolbars) {
        this.toolbars = toolbars;
        this.context = context;
    }

    @Override
    public int getCount() {
        return toolbars.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class ViewHolder {
        TextView function;
        ImageView imageView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_toolbar, null);
            viewHolder.function = view.findViewById(R.id.item_function);
            viewHolder.imageView = view.findViewById(R.id.item_imageToolbar);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();

        }
        viewHolder.function.setText(toolbars.get(i).getFunction());
        Glide.with(context).load(toolbars.get(i).getImage()).into(viewHolder.imageView);
        return view;
    }
}
