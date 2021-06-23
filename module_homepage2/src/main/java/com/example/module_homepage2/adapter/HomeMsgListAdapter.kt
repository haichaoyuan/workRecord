package com.shhxzq.ztb.ui.home.ui.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.module_homepage2.R
import com.example.module_homepage2.base.HotInfo


/**
 * 首页消息
 */
class HomeMsgListAdapter(data:MutableList<HotInfo>): BaseQuickAdapter<HotInfo, BaseViewHolder>(R.layout.item_list_home_info, data) {
    override fun convert(holder: BaseViewHolder, item: HotInfo) {
        holder.setText(R.id.home_item_title, item.title)
        holder.setText(R.id.home_item_time, item.updatetime)
        holder.setText(R.id.home_item_readNum, item.getReadNum() + " " + "阅读")

        val timeView = holder.getView<TextView>(R.id.home_item_time)
        val img = holder.getView<ImageView>(R.id.home_item_img)
//        if ("1" == item.getStickTop()) { //值 等于 1 为置顶
//            val drawable = ContextCompat.getDrawable(context, R.drawable.flag_hot)
//            drawable?.setBounds(
//                0, 0, drawable.minimumWidth,
//                drawable.minimumHeight
//            )
//
//            timeView.setCompoundDrawables(drawable, null, null, null)
//            timeView.setCompoundDrawablePadding(
//                context.resources
//                    .getDimensionPixelOffset(R.dimen.padding_small)
//            )
//        } else {
//            timeView.setCompoundDrawables(null, null, null, null)
//            timeView.setCompoundDrawablePadding(0)
//
//        }
        val placeholder = ContextCompat.getDrawable(context, R.drawable.image_place_holder_home)
        val drawableCrossFadeFactory =
            DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
        Glide.with(context)
            .load(item.cover)
            .placeholder(placeholder)
            .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
            .into(img)
    }
}