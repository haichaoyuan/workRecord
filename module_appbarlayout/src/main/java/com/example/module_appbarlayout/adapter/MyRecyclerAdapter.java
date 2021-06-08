package com.example.module_appbarlayout.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.module_appbarlayout.R;
import com.example.module_appbarlayout.item.Item;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter {

    public static final int VIEW_TYPE_ITEM = 1;
    public static final int VIEW_TYPE_FOOTER = 2;
    protected List<Item> mData;
    /**
     * 复用同一个View对象池,todo
     */
    private RecyclerView.RecycledViewPool mRecycledViewPool;
    /**
     * item高度
     */
    private int itemHeight;

    private RecyclerView mRecyclerView;
    /**
     * RecyclerView高度:??? todo
     */
    private int mRecyclerViewHeight;


    public MyRecyclerAdapter(@Nullable List<Item> data, RecyclerView mRecyclerView) {
        mData = data;
        this.mRecyclerView = mRecyclerView;
        mRecycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bar_item, parent, false);
            return new ItemViewHolder(view);
        } else {
            //Footer是最后留白的位置，以便最后一个item能够出发tab的切换,todo
            View view = new View(parent.getContext());
            Log.e("footer", "parentHeight: " + mRecyclerViewHeight + "--" + "itemHeight: " + itemHeight);
            view.setLayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            mRecyclerViewHeight - itemHeight));
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.mTitle.setText(mData.get(position).name);
            RecyclerView recyclerView = itemViewHolder.mRecyclerView;
            recyclerView.setRecycledViewPool(mRecycledViewPool);
            recyclerView.setHasFixedSize(false);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()) {
                @Override
                public boolean canScrollVertically() {
                    //保证嵌套滚动不冲突
                    return false;
                }

                @Override
                public void onLayoutCompleted(RecyclerView.State state) {
                    super.onLayoutCompleted(state);
                    mRecyclerViewHeight = mRecyclerView.getHeight();
                    itemHeight = itemViewHolder.itemView.getHeight();
                    Log.e("onLayoutCompleted",
                            "itemHeight: " + itemHeight + "--" + position);
                }
            });
            MyRecyclerDemoAdapter demoAdapter = new MyRecyclerDemoAdapter(mData.get(position).mSubItems);
            recyclerView.setAdapter(demoAdapter);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mData.size()) {
            return VIEW_TYPE_FOOTER;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private RecyclerView mRecyclerView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.bar_item_text);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.bar_item_recycler_view);
        }
    }
}