package com.ljx.indicator.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * 单类型适配器继承本类
 * User: ljx
 * Date: 2018/10/27
 * Time: 23:06
 */
abstract class BaseAdapter<T, VB : ViewDataBinding>(
    data: List<T>,
    @LayoutRes private val mLayoutId: Int
) : OnItemClickAdapter<T, BindingViewHolder<VB>>(data) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<VB> {
        val b: VB = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), mLayoutId, parent, false
        )
        return BindingViewHolder(b).apply {
            setOnItemClickListener(parent, this)
            setOnItemLongClickListener(parent, this)
            setOnItemChildClickListener(this)
        }
    }

    override fun onBindViewHolder(
        holder: BindingViewHolder<VB>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        holder.binding.onBindViewHolder(getItem(position), position, payloads)
    }

    protected open fun VB.onBindViewHolder(item: T, position: Int, payloads: MutableList<Any>) {
        onBindViewHolder(item, position)
    }

    override fun onBindViewHolder(holder: BindingViewHolder<VB>, position: Int) {}

    protected open fun VB.onBindViewHolder(item: T, position: Int) {}

    protected open fun getItem(position: Int): T = mData[position]

    fun VB.getString(@StringRes resId: Int, vararg formatArgs: Any?): String {
        return root.context.getString(resId, *formatArgs)
    }

    fun VB.getColor(@ColorRes resId: Int): Int {
        return root.context.resources.getColor(resId)
    }
}