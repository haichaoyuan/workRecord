package com.shhxzq.ztb.ui.home.ui.helper

import androidx.fragment.app.FragmentActivity
import com.example.module_homepage2.base.BusinessTransactionFragment
import java.lang.ref.WeakReference

/**
 * HelpFragment 的帮助类，处理生成与销毁
 */
class AllServiceHelper {
    private var mWeakActivityRef: WeakReference<FragmentActivity>? = null

    companion object {
        const val HELP_FRAGMENT = "helpFragment"

        val instance: AllServiceHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AllServiceHelper()
        }
    }

    fun init(activity: FragmentActivity): AllServiceHelper {
        mWeakActivityRef = WeakReference(activity)
        return this
    }

    fun getFragment(): HelpFragment {
        val manager = mWeakActivityRef?.get()?.supportFragmentManager
        if (manager == null) {
            throw RuntimeException("manager is not be null")
        }
        var helpFragment = manager.findFragmentByTag(HELP_FRAGMENT)

        if (helpFragment == null) {
            helpFragment = BusinessTransactionFragment.getInstance()
            manager.beginTransaction().add(helpFragment, HELP_FRAGMENT).commitAllowingStateLoss()
            manager.executePendingTransactions()
        }
        return (helpFragment as BusinessTransactionFragment)
    }

    fun release() {
        if (mWeakActivityRef != null && mWeakActivityRef!!.get() != null) {
            val manager = mWeakActivityRef!!.get()!!.supportFragmentManager
            var helpFragment = manager.findFragmentByTag(HELP_FRAGMENT)
            if (helpFragment != null) {
                manager.beginTransaction().remove(helpFragment).commitAllowingStateLoss()
                manager.executePendingTransactions()
            }
            mWeakActivityRef?.clear()
        }
    }
}