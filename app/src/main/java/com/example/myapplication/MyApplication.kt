package com.example.myapplication

import android.app.Application
import com.example.lib_util.utils.jump.IAppBarModule
import com.example.lib_util.utils.jump.ICommonViewModule
import com.example.lib_util.utils.jump.IHomePageModule
import com.example.lib_util.utils.jump.Router
import com.example.module_homepage2.module.*

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        //初始化模块间调用
        initRouter()
    }

    private fun initRouter() {
        Router.register(IHomePageModule::class.java.simpleName, HomepageModule())
        Router.register(IHomePageModule::class.java.simpleName, VideoRecordModule())
        Router.register(IHomePageModule::class.java.simpleName, StickListModule())
        Router.register(IAppBarModule::class.java.simpleName, AppBarModule())
        Router.register(IAppBarModule::class.java.simpleName, AutoTextModule())
        Router.register(ICommonViewModule::class.java.simpleName, CommonViewModule())
        Router.register(IAppBarModule::class.java.simpleName, StickHeaderScrollModule())
        Router.register(IAppBarModule::class.java.simpleName, StickListModule())
    }
}