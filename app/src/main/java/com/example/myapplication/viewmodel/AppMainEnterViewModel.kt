package com.example.module_homepage2.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.module_homepage2.entity.AllActivityEntity
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder

class AppMainEnterViewModel: ViewModel() {
    var allServicesEntity: AllActivityEntity? = null
        private set
    var CHANNELS = ArrayList<String>()

    fun init(context: Context) {
        allServicesEntity = getAllServicesFromJson(context, "all_module.json")
        if(allServicesEntity != null){
            CHANNELS.clear()
            for(item in allServicesEntity!!.result){
                CHANNELS.add(item.name)
            }
        }
    }

    private fun getAllServicesFromJson(context: Context, fileName:String):AllActivityEntity? {
        val stringBuilder = StringBuilder()
        try {
            val assetManager = context.assets
            val bf = BufferedReader(InputStreamReader(assetManager.open(fileName)))
            var line:String?
            while ((bf.readLine().also { line = it }) != null){
                stringBuilder.append(line)
            }
            // string -> List
            return Gson().fromJson(stringBuilder.toString(), AllActivityEntity::class.java)
        } catch (e:IOException){
            e.printStackTrace()
        }
        return null
    }
}