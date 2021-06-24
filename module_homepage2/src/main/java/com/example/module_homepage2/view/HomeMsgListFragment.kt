package com.shhxzq.ztb.ui.home.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.module_homepage2.R
import com.example.module_homepage2.base.HotInfo
import com.shhxzq.ztb.ui.home.ui.adapter.HomeMsgListAdapter

/**
 * 简单首页消息 Fragment
 *
 */
class HomeMsgListFragment(val index:Int): Fragment() {
    var recyclerView: RecyclerView? = null
    lateinit var homeMsgListAdapter: HomeMsgListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recycler, null)
        recyclerView= view.findViewById(R.id.recycler)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    fun initData() {
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        homeMsgListAdapter = HomeMsgListAdapter(ArrayList())
        recyclerView!!.adapter = homeMsgListAdapter


        var data = ArrayList<HotInfo>()
        for (i in 1..100){
            val item = HotInfo()
            item.title = "{$index}___title{$i}"
            data.add(item)
        }
        homeMsgListAdapter.setNewInstance(data)
    }

    fun updateUi(data:MutableList<HotInfo>){
        homeMsgListAdapter.setNewInstance(data)
    }

}