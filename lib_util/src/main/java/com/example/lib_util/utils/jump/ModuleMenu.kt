package com.example.lib_util.utils.jump

import java.io.Serializable

class ModuleMenu(val ids:Int) : Serializable {
    val menuName //菜单名字
            : String? = null
    val menuDesc //菜单详情
            : String? = null

    companion object {
        // =================================== 大型界面效果
        const val TYPE_HOME = 0 // 1
        const val TARGET_HOME_PAGE = 1 // 1
        const val TARGET_HOME_ALLSERVICE = 2 // 1
        const val TARGET_HOME_VIDEO_RECORD = 3 // 1
        const val TARGET_APPBAR_STICKHEADER_SCROLL = 4 // 1
        const val TARGET_APPBAR_STICK_LIST = 5 // 1
        // =================================== 视图
        const val TYPE_VIEW = 1 // 1
        const val TARGET_VIEW_COMMON = 100 // 1

        // =================================== 一些视图
        const val TYPE_APPBAR = 2 // 1
        const val TARGET_APPBAR_ENTER = 200 // 1
        const val TARGET_APPBAR_AUTOTEXT = 201 // 1


    }
}