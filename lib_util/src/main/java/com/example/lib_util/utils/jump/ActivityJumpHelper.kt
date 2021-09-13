package com.example.lib_util.utils.jump

import android.app.Activity

object ActivityJumpHelper {

    fun menuClick(context: Activity, menuRes: ModuleMenu){
        menuClickImp(context, menuRes)
    }

    fun menuClickImp(context: Activity, menuRes: ModuleMenu): Boolean{
        var ids = menuRes.ids
        val type = ids / 100
        when(type){
            IHomePageModule.TYPE ->{
                val moduleList: MutableList<IModule>? = Router.get(IHomePageModule::class.java.simpleName)
                moduleList?.run {
                    for (it in this) {
                        var jump = it.jump(context, ids)
                        if(jump){
                            break
                        }
                    }
                }
            }
            IAppBarModule.TYPE ->{
                val moduleList: MutableList<IModule>? = Router.get(IAppBarModule::class.java.simpleName)
                moduleList?.run {
                    for (it in this) {
                        var jump = it.jump(context, ids)
                        if(jump){
                            break
                        }
                    }
                }
            }
            ICommonViewModule.TYPE ->{
                val moduleList: MutableList<IModule>? = Router.get(ICommonViewModule::class.java.simpleName)
                moduleList?.run {
                    for (it in this) {
                        var jump = it.jump(context, ids)
                        if(jump){
                            break
                        }
                    }
                }
            }
            else -> {

            }
        }
        return true
    }
}