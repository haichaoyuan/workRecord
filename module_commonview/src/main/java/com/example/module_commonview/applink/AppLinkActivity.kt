package com.example.module_commonview.applink

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.module_commonview.R
import kotlinx.android.synthetic.main.activity_app_link.*

/**
 * 参考子
 */
class AppLinkActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_link)

        txt_click.setOnClickListener {
            var schemeValid = schemeValid()
            if(schemeValid){
                jump()
            }
        }
    }

    val data = "hxztb://oa/home"
    private fun schemeValid():Boolean{
        val manager = packageManager
        val action = Intent(Intent.ACTION_VIEW)
        action.data = Uri.parse(data)
        val list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER)
        return list != null && list.size > 0
    }

    private fun jump(){
        val action = Intent(Intent.ACTION_VIEW)
        action.data = Uri.parse(data)
        startActivity(action)
    }
}