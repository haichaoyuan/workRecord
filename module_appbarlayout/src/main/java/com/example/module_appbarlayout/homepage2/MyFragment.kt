package com.example.module_appbarlayout.homepage2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.module_appbarlayout.R


class MyFragment(val index: Int) : Fragment() {
    var recyclerView: RecyclerView? = null
    var data: MutableList<String> = ArrayList()
    var root:NestedScrollView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recycler, null)
        root = view.findViewById(R.id.root)
        recyclerView = view.findViewById(R.id.recycler)
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

    fun setNestScrollEnable(isScroll: Boolean) {
        root?.isNestedScrollingEnabled = isScroll
    }
}