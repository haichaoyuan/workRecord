package com.example.module_autotextview

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.lib_util.utils.UIUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_auto_text_view.*

class AutoTextViewActivity : AppCompatActivity() {
    var index = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_text_view)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            autoresizeTxt_title.text = DATA[index++ % (DATA.size)]
        }

        initBtn()

    }

    private fun initBtn() {
        autoresizeTxt_title.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            this.resources.getDimensionPixelSize(R.dimen.text_size_h4).toFloat()
        );
        autoresizeTxt_title.setMinTextSize(
            this.resources.getDimensionPixelSize(R.dimen.text_size_h10).toFloat()
        );
        autoresizeTxt_title.text = DATA[0]
        autoresizeTxt_title.setOnClickListener {
            autoresizeTxt_title.testFont()
        }

        initFirstCellWidth(autoresizeTxt_title, this)
    }

    /**
     * 首个 cell 宽度，半屏幕
     */
    private fun initFirstCellWidth(firstCell: View, context: Context) {
        val width: Int = UIUtil.getScreenWidth(context) / 2
        val layoutParams = firstCell.layoutParams as LinearLayout.LayoutParams
        layoutParams.width = width
        firstCell.layoutParams = layoutParams
    }


    val DATA = arrayListOf(
        "富国低碳环保混合汇添富国企创新股票",
        "东吴新经济混合汇添富国企创新股票汇添富国企创新股票",
        "汇添富国企创新股票",//37
        "富国天益价值混合A",//37
        "华安新丝路主题股票",//41
    )
}