package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lib_util.utils.jump.ModuleMenu
import com.example.module_homepage2.base.UIUtils
import com.example.myapplication.R

/**
 * @author yexiuliang on 2018/7/25
 */
class AllServiceDetail2Adapter(val mData: List<ModuleMenu>, val singleLineNum:Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClickListener: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_grid_module_menu, parent, false)
        return AllServiceDetailViewHolder(view)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //动态设置ImageView的宽高，根据自己每行item数量计算
        // https://blog.csdn.net/baiyuliang2013/article/details/51518868
        val dm = UIUtils.getScreentWidth(holder.itemView.context)
        val lp = RelativeLayout.LayoutParams((dm - UIUtils.dip2px(holder.itemView.context, 20F)) / singleLineNum, RelativeLayout.LayoutParams.WRAP_CONTENT);
        holder.itemView.setLayoutParams(lp)


        var menuRes = mData[position]
        var textView: TextView = holder.itemView.findViewById(R.id.menu_tv)
        textView.text = menuRes.menuName
        var textDesc: TextView = holder.itemView.findViewById(R.id.menu_desc)
        textDesc.text = menuRes.menuDesc

        if(onItemClickListener != null){
            holder.itemView.tag = menuRes
            holder.itemView.setOnClickListener(onItemClickListener)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}

class AllServiceDetailViewHolder(val view: View) : RecyclerView.ViewHolder(view)