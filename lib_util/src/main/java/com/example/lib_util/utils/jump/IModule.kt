package com.example.lib_util.utils.jump

import android.content.Context

/**
 * Created by dylan on 2017/11/4.
 * Desc: 基础Module，第个Module都要含有动态跳转方法
 */
interface IModule {
    fun jump(context: Context, ids:Int):Boolean
}