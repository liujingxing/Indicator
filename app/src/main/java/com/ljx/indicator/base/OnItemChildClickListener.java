package com.ljx.indicator.base;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * User: ljx
 * Date: 2018/10/28
 * Time: 00:14
 */
public interface OnItemChildClickListener {

    void onClick(@NonNull View view, int position);
}
