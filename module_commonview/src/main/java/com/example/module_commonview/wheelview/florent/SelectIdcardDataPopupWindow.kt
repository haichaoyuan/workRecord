package com.shhxzq.ztb.ui.oa.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import com.example.module_commonview.R
import com.example.module_commonview.wheelview.florent.wheelview.DateWheelLayout
import com.example.module_commonview.wheelview.florent.wheelview.annotation.DateMode
import com.example.module_commonview.wheelview.florent.wheelview.entity.DateEntity
import com.example.module_commonview.wheelview.florent.wheelview.formatimpl.BirthdayFormatter
import java.util.*

/**
 * 选择身份证日期页面
 */
class SelectIdcardDataPopupWindow(val context: Context)
    : PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT), View.OnClickListener {

    var data: MutableList<String> = ArrayList()

    init {
        initViews()
    }

    private fun initViews() {
        val contentview: View = LayoutInflater.from(context).inflate(R.layout.view_select_idcard_data, null)
        val iv_cancel = contentview.findViewById<TextView>(R.id.dialog_modal_cancel)
        iv_cancel.setOnClickListener(this)
        val iv_sure = contentview.findViewById<TextView>(R.id.dialog_modal_ok)
        iv_sure.setOnClickListener(this)
        val iv_title = contentview.findViewById<TextView>(R.id.dialog_modal_title)
        iv_title.setText("长期")
        val data_wheel = contentview.findViewById<DateWheelLayout>(R.id.data_wheel)
        configDateWheelLayout(data_wheel)

        contentView = contentview
        isFocusable = true
        isTouchable = true
        isOutsideTouchable = false
    }

    /**
     * 配置 dataWheel
     */
    private fun configDateWheelLayout(dataWheel: DateWheelLayout?) {
        dataWheel?.run {
            val calendar = Calendar.getInstance()
            val currentYear = calendar[Calendar.YEAR]
            val currentMonth = calendar[Calendar.MONTH] + 1
            val currentDay = calendar[Calendar.DAY_OF_MONTH]
            val startValue = DateEntity.target(currentYear - MAX_AGE, 1, 1)
            val endValue = DateEntity.target(currentYear, currentMonth, currentDay)
            //设置范围
            setRange(startValue, endValue, endValue)
            setDateMode(DateMode.YEAR_MONTH_DAY)
            setDateFormatter(BirthdayFormatter())
        }
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.dialog_modal_cancel) {
            onCancel()
            dismiss()
        } else if (id == R.id.dialog_modal_ok) {
            onOk()
            dismiss()
        }
    }

    private fun onOk() {
        Toast.makeText(context, "ok", Toast.LENGTH_LONG).show()
    }

    private fun onCancel() {
        Toast.makeText(context, "onCancel", Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val MAX_AGE = 100
    }
}