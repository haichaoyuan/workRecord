package com.example.module_homepage2.entity

import com.example.lib_util.utils.jump.ModuleMenu

data class AllActivityEntity(val result:List<ActivityDetailEntity>)

data class ActivityDetailEntity(val name:String, val values:List<ModuleMenu>)