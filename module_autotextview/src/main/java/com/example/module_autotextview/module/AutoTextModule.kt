package com.example.module_homepage2.module

import android.content.Context
import android.content.Intent
import com.example.lib_util.utils.jump.IAppBarModule
import com.example.lib_util.utils.jump.ModuleMenu
import com.example.module_autotextview.AutoTextViewActivity

class AutoTextModule : IAppBarModule {
    override fun jump(context: Context, ids: Int):Boolean {
        return when (ids) {
            ModuleMenu.TARGET_APPBAR_AUTOTEXT -> {
                context.startActivity(Intent(context, AutoTextViewActivity::class.java))
                true
            }
            else -> {
                false
            }
        }
    }
}