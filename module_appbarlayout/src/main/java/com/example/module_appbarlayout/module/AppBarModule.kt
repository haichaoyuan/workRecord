package com.example.module_homepage2.module

import android.content.Context
import android.content.Intent
import com.example.lib_util.utils.jump.IAppBarModule
import com.example.lib_util.utils.jump.ModuleMenu
import com.example.module_appbarlayout.AppbarLayoutEnterActivity

class AppBarModule : IAppBarModule {
    override fun jump(context: Context, ids: Int):Boolean {
        return when (ids) {
            ModuleMenu.TARGET_APPBAR_ENTER -> {
                context.startActivity(Intent(context, AppbarLayoutEnterActivity::class.java))
                true
            }
            else -> {
                false
            }
        }
    }
}