package com.ljx.indicator.base;

import android.view.View;
import android.view.ViewGroup;

/**
 * User: ljx
 * Date: 2017/5/10
 * Time: 11:05
 */
public interface OnItemLongClickListener<T> {

    boolean onItemLongClick(ViewGroup parent, View view, T t, int position);
}