package com.example.module_appbarlayout.adapter;

import androidx.annotation.Nullable;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.module_appbarlayout.R;
import com.example.module_appbarlayout.item.Item;

import java.util.List;

/**
 * @author yexiuliang on 2018/7/25
 */
public class MyRecyclerDemoAdapter extends BaseQuickAdapter<Item.SubItem, BaseViewHolder> {

    public MyRecyclerDemoAdapter(@Nullable List<Item.SubItem> data) {
        super(R.layout.bar_subitem, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Item.SubItem item) {
        holder.setText(R.id.demo_subitem_text,item.name);
        holder.setText(R.id.demo_subitem_desc,item.desc);
    }
}
