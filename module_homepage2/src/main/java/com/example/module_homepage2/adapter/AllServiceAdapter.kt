package com.example.module_homepage2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.module_homepage2.R
import com.example.module_homepage2.entity.ServiceDetailEntity

class AllServiceAdapter(val mData: List<ServiceDetailEntity>, val mRecyclerView: RecyclerView) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_ITEM = 1
        const val VIEW_TYPE_FOOTER = 2
    }

    private var mRecycledViewPool: RecyclerView.RecycledViewPool

    private var itemHeight = 0
    private var mRecyclerViewHeight = 0

    init {
        mRecycledViewPool = RecyclerView.RecycledViewPool()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_service,null, false)
            return AllServiceViewHolder(view)
        } else {
            // Footer是最后留白的位置，以便最后一个item能够出发tab的切换,
            val view = View(parent.context)
            view.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                mRecyclerViewHeight - itemHeight
            )
            return AllServiceFootViewHolder(view)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_ITEM) {
            val itemViewHolder: AllServiceViewHolder = holder as AllServiceViewHolder
            itemViewHolder.mTitle.text = mData[position].name
            // init recycler
            var recyclerView: RecyclerView = itemViewHolder.mRecyclerView
            recyclerView.setRecycledViewPool(mRecycledViewPool)
            recyclerView.setHasFixedSize(true)
            recyclerView.isNestedScrollingEnabled = false
            recyclerView.layoutManager = object : GridLayoutManager(recyclerView.context, 5) {
                override fun canScrollVertically(): Boolean {
                    // 保证嵌套滚动不冲突
                    return false
                }

                override fun onLayoutCompleted(state: RecyclerView.State?) {
                    super.onLayoutCompleted(state)
                    mRecyclerViewHeight = mRecyclerView.height
                    itemHeight = itemViewHolder.itemView.height
                }
            }
            val adapter = AllServiceDetail2Adapter(mData[position].values)
            recyclerView.adapter = adapter
            // 隐藏第一个 文本
            if(position == 0){
                itemViewHolder.mTitle.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return mData.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == mData.size) {
            return VIEW_TYPE_FOOTER
        } else {
            return VIEW_TYPE_ITEM
        }
    }


}

class AllServiceViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val mTitle: TextView
    val mRecyclerView: RecyclerView

    init {
        mTitle = view.findViewById(R.id.bar_item_text)
        mRecyclerView = view.findViewById(R.id.bar_item_recycler_view)
    }

}

class AllServiceFootViewHolder(val view: View) : RecyclerView.ViewHolder(view)