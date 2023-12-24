package com.ljx.indicator.base

import android.view.View
import android.view.ViewGroup

/**
 * User: ljx
 * Date: 2017/5/10
 * Time: 11:05
 */
interface OnItemClickListener<T> {
    fun onItemClick(parent: ViewGroup, view: View, item: T, position: Int)
}