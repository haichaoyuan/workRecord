package com.example.module_commonview.applink

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.module_commonview.R
import kotlinx.android.synthetic.main.activity_app_link.*
import java.util.ArrayList

/**
 * 参考子
 */
class AppLinkActivity : FragmentActivity() {
    lateinit var selectColumnList: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_link)

        txt_click.setOnClickListener {
            var schemeValid = schemeValid()
            if(schemeValid){
                jump()
            }
        }
        txt_test.setOnClickListener {
            val array = ArrayList<String>()
            array.add("abc")
            selectColumnList = array
        }
    }

    val data = "hxztb://oa/home"
    val data2 = "hxztb://main?tab_index=4"
    private fun schemeValid():Boolean{
        val manager = packageManager
        val action = Intent(Intent.ACTION_VIEW)
        action.data = Uri.parse(data2)
        val list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER)
        return list != null && list.size > 0
    }

    private fun jump(){
        val action = Intent(Intent.ACTION_VIEW)
        action.data = Uri.parse(data2)
        startActivity(action)
    }
}