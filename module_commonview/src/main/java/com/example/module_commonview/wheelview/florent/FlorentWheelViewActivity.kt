package com.example.module_commonview.wheelview.florent

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.example.module_commonview.R
import com.shhxzq.ztb.ui.oa.util.SelectIdcardDataPopupWindow
import kotlinx.android.synthetic.main.activity_florent_wheel_view.*

class FlorentWheelViewActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_florent_wheel_view)

        number_wheelview.setRange(0, 100, 1)
    }

    fun onFlorentClick(view: View?) {
        val popWindow = SelectIdcardDataPopupWindow(this)
        popWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0)
    }
}