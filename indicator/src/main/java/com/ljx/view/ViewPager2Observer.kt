package com.ljx.view

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

/**
 * User: ljx
 * Date: 2023/12/27
 * Time: 10:20
 */
class ViewPager2Observer(
    private val indicatorView: IndicatorView,
    private val viewPager2: ViewPager2,
    private val autoRefresh: Boolean = true,
) {
    private var attached = false
    private var adapter: RecyclerView.Adapter<*>? = null
    private var dataSetObserver: RecyclerView.AdapterDataObserver? = null

    private var onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {

        override fun onPageScrolled(position: Int, offset: Float, offsetPixels: Int) {
            indicatorView.scrollSlider(position, offset)
        }
    }

    fun attach() {
        check(!attached) { "ViewPager2Observer is already attached" }
        val adapter = viewPager2.adapter
        checkNotNull(adapter) { "ViewPager2Observer attached before ViewPager2 has an " + "adapter" }
        attached = true
        viewPager2.registerOnPageChangeCallback(onPageChangeCallback)

        // Now we'll populate ourselves from the pager adapter, adding an observer if
        // autoRefresh is enabled
        if (autoRefresh) {
            // Register our observer on the new adapter
            val dataSetObserver = OnDataSetObserver().also { this.dataSetObserver = it }
            adapter.registerAdapterDataObserver(dataSetObserver)
        }
        indicatorView.setSliderCount(adapter.itemCount)
        indicatorView.scrollSlider(viewPager2.currentItem, 0f)
        this.adapter = adapter
    }

    fun detach() {
        if (autoRefresh) {
            dataSetObserver?.let { adapter?.unregisterAdapterDataObserver(it) }
            dataSetObserver = null
        }
        viewPager2.unregisterOnPageChangeCallback(onPageChangeCallback)
        adapter = null
        attached = false
    }

    fun isAttached() = attached

    private inner class OnDataSetObserver : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            val count = adapter?.itemCount ?: 0
            indicatorView.setSliderCount(count)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            onChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            onChanged()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            onChanged()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            onChanged()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            onChanged()
        }
    }
}