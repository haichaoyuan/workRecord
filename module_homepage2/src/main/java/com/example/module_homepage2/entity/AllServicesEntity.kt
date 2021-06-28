package com.example.module_homepage2.entity

import com.example.module_homepage2.base.AppMenuRes

data class AllServicesEntity(val result:List<ServiceDetailEntity>)

data class ServiceDetailEntity(val name:String, val values:List<AppMenuRes>)