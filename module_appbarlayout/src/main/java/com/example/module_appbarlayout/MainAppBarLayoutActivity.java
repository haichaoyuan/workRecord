package com.example.module_appbarlayout;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.module_appbarlayout.adapter.MyRecyclerAdapter;
import com.example.module_appbarlayout.item.Item;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainAppBarLayoutActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{
    RecyclerView mHeadRecy;
    RecyclerView mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_appbarlayout);

        init();
//        init3();
    }

    private TabLayout mTabLayout;
    private AppBarLayout mAppBar;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayoutManager mLinearLayoutManager;

    /**
     * 数据源
     */
    private ArrayList<Item> mItems;

    /**
     * 是否处于滚动状态，避免连锁反应
     */
    private boolean isScroll;

    /**
     * 平滑滚动 Scroller
     */
    private RecyclerView.SmoothScroller mSmoothScroller;

    private void init() {
        mTabLayout = findViewById(R.id.tab_layout);
        mRecyclerView = findViewById(R.id.recycler_view);
        mAppBar = findViewById(R.id.appbar);
        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        ((CoordinatorLayout.LayoutParams) mAppBar.getLayoutParams()).setBehavior(new FixAppBarLayoutBehavior());
        initRecyclerView();
        initTabLayout();
        initData();
    }

    private void initRecyclerView() {
        mSmoothScroller = new LinearSmoothScroller(this) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }

            @Nullable
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return mLinearLayoutManager.computeScrollVectorForPosition(targetPosition);
            }
        };

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {//非滚动
                    isScroll = false;
                } else {
                    isScroll = true;
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //滑动RecyclerView list的时候，根据最上面一个Item的position来切换tab
//                mTabLayout.setScrollPosition(mLinearLayoutManager.findFirstVisibleItemPosition(), 0, true);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                TabLayout.Tab tabAt = mTabLayout.getTabAt(layoutManager.findFirstVisibleItemPosition());
                if (tabAt != null && !tabAt.isSelected()) {
                    tabAt.select();
                }

                // -1 是顶部, 1是底部
                // 可以继续往上滑动(未滑动到顶部)
//                boolean isNotBottom = recyclerView.canScrollVertically(1);
//                boolean isNotTop = recyclerView.canScrollVertically(-1);
//                if (!isNotTop){
//                    swipeRefreshLayout.setEnabled(true);
//                } else {
//                    swipeRefreshLayout.setEnabled(false);
//                }


            }
        });
    }
    
    private void initTabLayout() {
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorAccent));
        mTabLayout.setTabTextColors(Color.BLACK, ContextCompat.getColor(this, R.color.colorAccent));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //点击tab的时候，RecyclerView自动滑到该tab对应的item位置
                int position = tab.getPosition();
                if (!isScroll) {
                    // 不会滚动到顶部，出现了就不滚动
//                    mRecyclerView.smoothScrollToPosition(position);
                    // 没有动画
//                    mLinearLayoutManager.scrollToPositionWithOffset(position, 0);
                    // 有动画且滚动到顶部
                    mSmoothScroller.setTargetPosition(position);
                    mLinearLayoutManager.startSmoothScroll(mSmoothScroller);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initData() {
        mItems = new ArrayList<>();
        Item item = new Item();
        item.name = "便民生活";
        ArrayList<Item.SubItem> itemsub = new ArrayList<>();
        itemsub.add(new Item.SubItem("便民生活----", "这是描述"));
        itemsub.add(new Item.SubItem("充值中心----", "这是描述"));
        itemsub.add(new Item.SubItem("便民生活----", "这是描述"));
        itemsub.add(new Item.SubItem("便民生活----", "这是描述"));
        itemsub.add(new Item.SubItem("便民生活----", "这是描述"));
        item.mSubItems = itemsub;

        Item item1 = new Item();
        item1.name = "财富管理";
        ArrayList<Item.SubItem> itemsub1 = new ArrayList<>();
        itemsub1.add(new Item.SubItem("余额宝======", "这是描述"));
        itemsub1.add(new Item.SubItem("花呗======", "这是描述"));
        itemsub1.add(new Item.SubItem("芝麻信用=========", "这是描述"));
        itemsub1.add(new Item.SubItem("蚂蚁借呗======", "这是描述"));
        item1.mSubItems = itemsub1;

        Item item2 = new Item();
        item2.name = "资金往来";
        ArrayList<Item.SubItem> itemsub2 = new ArrayList<>();
        itemsub2.add(new Item.SubItem("资金往来", "这是描述"));
        itemsub2.add(new Item.SubItem("资金往来", "这是描述"));
        item2.mSubItems = itemsub2;

        Item item3 = new Item();
        item3.name = "娱乐购物";
        ArrayList<Item.SubItem> itemsub3 = new ArrayList<>();
        itemsub3.add(new Item.SubItem("娱乐购物", "这是描述"));
        itemsub3.add(new Item.SubItem("娱乐购物", "这是描述"));
        itemsub3.add(new Item.SubItem("娱乐购物", "这是描述"));
        item3.mSubItems = itemsub3;

        mItems.add(item);
        mItems.add(item1);
        mItems.add(item2);
        mItems.add(item3);
        //这里模仿接口回调，动态设置TabLayout和RecyclerView 相同数据。保证position
        for (Item it : mItems) {
            mTabLayout.addTab(mTabLayout.newTab().setText(it.name));
        }
        MyRecyclerAdapter myAdapter = new MyRecyclerAdapter(mItems, mRecyclerView);
        mRecyclerView.setAdapter(myAdapter);

    }
    

// =========================================================================================
// =================================== appbarlayout
// =========================================================================================


    @Override
    protected void onResume() {
        super.onResume();
        mAppBar.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAppBar.removeOnOffsetChangedListener(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        swipeRefreshLayout.setEnabled(verticalOffset == 0);
    }
}