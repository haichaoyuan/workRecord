package com.example.module_stickheaderscrollview.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.module_stickheaderscrollview.R;
import com.example.module_stickheaderscrollview.StickHeadScrollViewActivity;
import com.example.module_stickheaderscrollview.ui.adapter.ReListAdapter;

/**
 * @author: JiangWeiwei
 * @time: 2017/11/9-11:34
 * @email:
 * @desc:
 */
public class ChildScrollViewFragment extends Fragment {
    private static final String KEY_FRAGMENT_INT = "KEY_FRAGMENT_INT";
    private ReListAdapter mReListAdapter;
    private boolean move;
    private boolean mIsLeftTouch = true;
    private int mOldLeftIndex;
    private StickHeadScrollViewActivity mParentActivity;
    private TextView mTvTitle;

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
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.demo_child_scroll, container, false);
        initView(view);
        mTvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"存在点击事件",Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void initView(View view) {
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
    }

    public static Fragment newInstance(int position) {
        ChildScrollViewFragment fragment = new ChildScrollViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_FRAGMENT_INT, position);
        fragment.setArguments(bundle);
        return fragment;
    }
}
