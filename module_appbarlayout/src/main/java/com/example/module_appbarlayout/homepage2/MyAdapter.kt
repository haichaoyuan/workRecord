package com.example.module_appbarlayout.homepage2

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.module_appbarlayout.R

class MyAdapter(data:MutableList<String>): BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_center, data) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.txt, item)
    }
}