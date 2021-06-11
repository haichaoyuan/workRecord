package com.example.module_stickheaderscrollview.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.module_stickheaderscrollview.R;
import com.example.module_stickheaderscrollview.StickHeadScrollViewActivity;
import com.example.module_stickheaderscrollview.stickscrollview.ChildWebView;
import com.example.module_stickheaderscrollview.stickscrollview.layoutmanager.NoSlideLinearLayoutManager;
import com.example.module_stickheaderscrollview.ui.adapter.RVRightListAdapter;
import com.example.module_stickheaderscrollview.ui.adapter.ReListAdapter;
import com.example.module_stickheaderscrollview.ui.listener.OnRVItemClickListener;


/**
 * @author: JiangWeiwei
 * @time: 2017/11/9-11:34
 * @email:
 * @desc:
 */
public class ChildRecyclerViewFragment extends LazyFragment {
    private static final String KEY_FRAGMENT_INT = "KEY_FRAGMENT_INT";
    private RecyclerView mChildRecyclerview;
    private RecyclerView mChildRecyclerviewRight;
    private ReListAdapter mReListAdapter;
    private NoSlideLinearLayoutManager mLlRight;
    private boolean move;
    private boolean mIsLeftTouch = true;
    private int mOldLeftIndex;
    private RecyclerViewListener mRecyclerViewListener;
    private StickHeadScrollViewActivity mParentActivity;
    private ChildWebView webview;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null && context instanceof StickHeadScrollViewActivity) {
            mParentActivity = (StickHeadScrollViewActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.recyclerview, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mChildRecyclerviewRight.post(new Runnable() {
//            @Override
//            public void run() {
//                ViewGroup.LayoutParams layoutParams = mChildRecyclerviewRight.getLayoutParams();
//                layoutParams.height = 1400;
//                mChildRecyclerviewRight.setLayoutParams(layoutParams);
//            }
//        });

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.loadUrl("https://www.baidu.com/?tn=22073068_oem_dg");
    }


    private void initView(View view) {
        mChildRecyclerview = (RecyclerView) view.findViewById(R.id.child_recyclerview);
        mChildRecyclerviewRight = (RecyclerView) view.findViewById(R.id.child_recyclerview_right);
        webview = (ChildWebView) view.findViewById(R.id.webview);
    }

    public static Fragment newInstance(int position) {
        ChildRecyclerViewFragment fragment = new ChildRecyclerViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_FRAGMENT_INT, position);
        fragment.setArguments(bundle);
        return fragment;
    }


    class RecyclerViewListener extends RecyclerView.OnScrollListener {

        private int mIndex;


        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (!mIsLeftTouch) {
                leftLocation();
            }
            mIsLeftTouch = false;
        }


        public void setIndex(int index) {
            this.mIndex = index;
        }


        private void leftLocation() {
            int firstItem = mLlRight.findFirstVisibleItemPosition();
            int newRightPos = firstItem / 2;
            if (newRightPos != mOldLeftIndex) {
                mReListAdapter.setSelectIndex(newRightPos);
                mOldLeftIndex = newRightPos;
            }
        }

    }

    @Override
    void lazyFetchData() {
        initUI();
    }

    private void initUI() {
        mRecyclerViewListener = new RecyclerViewListener();

        mLlRight = new NoSlideLinearLayoutManager(getActivity());

        mReListAdapter = new ReListAdapter();
        LinearLayoutManager ll = new NoSlideLinearLayoutManager(getActivity());
        mChildRecyclerview.setLayoutManager(ll);
        mChildRecyclerview.setAdapter(mReListAdapter);
        mReListAdapter.setOnRVItemClickListener(new OnRVItemClickListener<String>() {
            @Override
            public void onClick(String s, int index) {
                mIsLeftTouch = true;
                if (mOldLeftIndex != index) {
                    int scrollIndex = index * 2;
                    mRecyclerViewListener.setIndex(scrollIndex);
                    mLlRight.scrollToPositionWithOffset(scrollIndex, 0);
                    mOldLeftIndex = index;
                }
            }
        });


        mChildRecyclerviewRight.setLayoutManager(mLlRight);
        mChildRecyclerviewRight.setAdapter(new RVRightListAdapter());
        mChildRecyclerviewRight.addOnScrollListener(mRecyclerViewListener);
    }
}
