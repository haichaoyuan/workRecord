package com.example.module_homepage2.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * Created by gerong on 6/21/17.
 */

public class GAImageView extends androidx.appcompat.widget.AppCompatImageView implements GAView {

    private String mGAElementString = null;

    public GAImageView(Context context) {
        super(context);
    }

    public GAImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setGAString(String elementId) {
        mGAElementString = elementId;
    }

    @Override
    public String getGAString() {
        return mGAElementString;
    }

    @Override
    public boolean performClick() {
        // 由于切换fragment打点的时候 super.performClick() 会把全局pageid pageuuid改变掉，所以先把值取出来
//        String pageId = StatisticsUtil.getCurrentPageId();
//        String pageUuid = StatisticsUtil.getCurrentpageUUID();
//        if (super.performClick()) {
//            //只有在OnClickListener被调用的时候才打点
//            GAHelper.instance().uploadGA(this, GAHelper.ACTION_CLICK, pageId, pageUuid);
//            return true;
//        }
        return false;
    }
}
