package com.ljx.indicator.base

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * User: ljx
 * Date: 2017/5/10
 * Time: 11:00
 */
abstract class OnItemClickAdapter<T, VH : RecyclerView.ViewHolder>(
    var mData: List<T>
) : RecyclerView.Adapter<VH>() {

    private var mOnItemClickListener: OnItemClickListener<T>? = null
    private var mOnItemLongClickListener: OnItemLongClickListener<T>? = null

    @JvmField
    protected var mOnChildClick: OnItemChildClickListener? = null
    private var clickIds: MutableList<Int>? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<T>?) {
        mOnItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener<T>?) {
        mOnItemLongClickListener = onItemLongClickListener
    }

    fun setOnItemChildClick(onChildClick: OnItemChildClickListener?) {
        mOnChildClick = onChildClick
    }

    protected fun setOnItemClickListener(parent: ViewGroup, holder: VH) {
        mOnItemClickListener?.let {
            holder.itemView.setOnClickListener { v: View ->
                val position = holder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@setOnClickListener
                it.onItemClick(parent, v, mData[position], position)
            }
        }
    }

    protected fun setOnItemLongClickListener(parent: ViewGroup, holder: VH) {
        mOnItemLongClickListener?.let {
            holder.itemView.setOnLongClickListener { v: View ->
                val position = holder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@setOnLongClickListener true
                it.onItemLongClick(parent, v, mData[position], position)
            }
        }
    }

    protected fun setOnItemChildClickListener(holder: VH) {
        mOnChildClick?.let {
            clickIds?.forEach { id ->
                holder.itemView.findViewById<View>(id)?.setOnClickListener { view ->
                    val position = holder.bindingAdapterPosition
                    if (position == RecyclerView.NO_POSITION) return@setOnClickListener
                    it.onClick(view, position)
                }
            }
        }
    }

    protected fun addClickIds(vararg ids: Int) {
        if (clickIds == null) {
            clickIds = mutableListOf()
        }
        clickIds?.addAll(ids.toTypedArray())
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    @get:JvmName("getData")
    val data = mData as MutableList
}