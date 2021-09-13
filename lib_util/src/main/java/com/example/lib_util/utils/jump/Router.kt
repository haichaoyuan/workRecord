package com.example.lib_util.utils.jump

import android.util.Log
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by dylan on 2018/1/3.
 * Desc: 路由，用于各模块间通信
 *
 * 1. 每个模块，声明继承自 IModule 的模块接口，来对外界通知其可提供的功能
 * 2. 模块内，实现该模块接口
 * 3. 主模块，统一初始化所有模块接口实现
 * 4. 单个模块想通信，只需要找到对应模块即可调用
 */
object Router {

    //组件map
    private val moduleMap: HashMap<String, MutableList<IModule>> = HashMap()

    //注册组件的实现
    fun register(name: String, impl: IModule) {
        if(!moduleMap.containsKey(name)){
            moduleMap.put(name, ArrayList<IModule>())
        }
        moduleMap.get(name)!!.add(impl)
    }

    //获取相应组件的实现
    fun get(name: String): MutableList<IModule>? {
        return moduleMap[name]
    }
}