package com.example.module_appbarlayout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.module_appbarlayout.R;


public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder>{

    private int itemNum = 0;
    private Context context;
    public ContentAdapter(Context context, int itemNum){
        this.itemNum = itemNum;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bar_rv_item_content,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 布局，万能 RecyclerViewAdapter
        TextView viewById = holder.itemView.findViewById(R.id.txt);
        viewById.setText(position+ "个");
    }

    @Override
    public int getItemCount() {
        return itemNum;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}