package com.ljx.view

import android.database.DataSetObserver
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

/**
 * User: ljx
 * Date: 2023/12/27
 * Time: 10:20
 */
class ViewPagerObserver(
    private val indicatorView: IndicatorView,
    private val viewPager: ViewPager,
    private val autoRefresh: Boolean = true,
) {
    private var attached = false
    private var adapter: PagerAdapter? = null
    private var dataSetObserver: DataSetObserver? = null

    private val onAdapterChangeListener =
        ViewPager.OnAdapterChangeListener { _, _, newAdapter -> setAdapter(newAdapter) }

    private var onPageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {

        override fun onPageScrolled(position: Int, offset: Float, offsetPixels: Int) {
            indicatorView.scrollSlider(position, offset)
        }
    }

    fun attach() {
        check(!attached) { "ViewPagerObserver is already attached" }
        val adapter = viewPager.adapter
        attached = true
        viewPager.addOnPageChangeListener(onPageChangeListener)
        viewPager.addOnAdapterChangeListener(onAdapterChangeListener)

        indicatorView.scrollSlider(viewPager.currentItem, 0f)
        setAdapter(adapter)
    }

    fun detach() {
        if (autoRefresh) {
            dataSetObserver?.let { adapter?.unregisterDataSetObserver(it) }
            dataSetObserver = null
        }
        viewPager.removeOnPageChangeListener(onPageChangeListener)
        viewPager.removeOnAdapterChangeListener(onAdapterChangeListener)
        adapter = null
        attached = false
    }

    fun isAttached() = attached

    private fun setAdapter(newAdapter: PagerAdapter?) {
        if (adapter === newAdapter) return
        if (autoRefresh) {
            dataSetObserver?.let { adapter?.registerDataSetObserver(it) }
        }
        if (newAdapter != null && autoRefresh) {
            // Register our observer on the new adapter
            val observer =
                dataSetObserver ?: OnDataSetObserver().also { dataSetObserver = it }
            newAdapter.registerDataSetObserver(observer)
        }
        indicatorView.setSliderCount(newAdapter?.count ?: 0)
        adapter = newAdapter
    }

    private inner class OnDataSetObserver : DataSetObserver() {
        override fun onInvalidated() {
            onChanged()
        }

        override fun onChanged() {
            val count = adapter?.count ?: 0
            indicatorView.setSliderCount(count)
        }
    }
}