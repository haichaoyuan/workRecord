package com.example.module_homepage2.module

import android.content.Context
import android.content.Intent
import com.example.lib_util.utils.jump.IHomePageModule
import com.example.lib_util.utils.jump.ModuleMenu
import com.example.module_homepage2.AllServiceActivity
import com.example.module_homepage2.HomePage2Activity

class HomepageModule : IHomePageModule {
    override fun jump(context: Context, ids: Int):Boolean {
        return when (ids) {
            ModuleMenu.TARGET_HOME_PAGE -> {
                context.startActivity(Intent(context, HomePage2Activity::class.java))
                true
            }
            ModuleMenu.TARGET_HOME_ALLSERVICE -> {
                context.startActivity(Intent(context, AllServiceActivity::class.java))
                true
            }
            else -> {
                false
            }
        }
    }
}