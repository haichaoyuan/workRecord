package com.example.module_homepage2.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.example.lib_util.utils.UIUtil.getScreenWidth
import com.example.module_homepage2.R
import com.example.module_homepage2.base.AppMenuRes
import com.example.module_homepage2.base.UIUtils

/**
 * @author yexiuliang on 2018/7/25
 */
class AllServiceDetail2Adapter(val mData: List<AppMenuRes>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_grid_home_menu, parent, false)
        return AllServiceDetailViewHolder(view)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //动态设置ImageView的宽高，根据自己每行item数量计算
        // https://blog.csdn.net/baiyuliang2013/article/details/51518868
        val dm = UIUtils.getScreentWidth(holder.itemView.context)
        val lp = RelativeLayout.LayoutParams((dm - UIUtils.dip2px(holder.itemView.context, 20F)) / 5, RelativeLayout.LayoutParams.WRAP_CONTENT);
        holder.itemView.setLayoutParams(lp)


        var menuRes = mData[position]
        var textView: TextView = holder.itemView.findViewById(R.id.menu_tv)
        textView.text = menuRes.menuName
        val drawableCrossFadeFactory =
            DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
        var imageView: ImageView = holder.itemView.findViewById(R.id.menu_img)
        Glide.with((holder.itemView.context as Activity)).load(R.mipmap.icon_ipo)
            .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory)).into(imageView)
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}

class AllServiceDetailViewHolder(val view: View) : RecyclerView.ViewHolder(view)