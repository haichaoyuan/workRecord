package com.example.module_appbarlayout.homepage2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.module_appbarlayout.R


class MyFragment(val index:Int): Fragment() {
    var recyclerView: RecyclerView? = null
    var data: MutableList<String> = ArrayList()

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
        var i = 'A'.toInt()
        while (i < 'z'.toInt()) {
            data.add("$index=" + i.toChar())
            i++
        }
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView!!.adapter = MyAdapter(data)
    }

}