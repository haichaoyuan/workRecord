package com.example.module_homepage2.module

import android.content.Context
import android.content.Intent
import com.example.lib_util.utils.jump.IHomePageModule
import com.example.lib_util.utils.jump.ModuleMenu
import com.example.module_videorecord.VideoRecordEnterActivity

class VideoRecordModule : IHomePageModule {
    override fun jump(context: Context, ids: Int):Boolean {
        return when (ids) {
            ModuleMenu.TARGET_HOME_VIDEO_RECORD -> {
                context.startActivity(Intent(context, VideoRecordEnterActivity::class.java))
                true
            }
            else -> {
                false
            }
        }
    }
}