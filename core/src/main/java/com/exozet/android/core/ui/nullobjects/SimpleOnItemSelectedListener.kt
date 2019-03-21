package com.exozet.android.core.ui.nullobjects

import android.view.View
import android.widget.Adapter
import android.widget.AdapterView

open class SimpleOnItemSelectedListener : AdapterView.OnItemSelectedListener {

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}

inline fun <reified T> AdapterView<T>.onItemSelected(crossinline block: (parent: AdapterView<*>?, view: View?, position: Int, id: Long) -> Unit) where T : Adapter {
    onItemSelectedListener = object : SimpleOnItemSelectedListener() {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) = block(parent, view, position, id)
    }
}

inline fun <reified T> AdapterView<T>.onNothingSelected(crossinline block: (parent: AdapterView<*>?) -> Unit) where T : Adapter {

    onItemSelectedListener = object : SimpleOnItemSelectedListener() {
        override fun onNothingSelected(parent: AdapterView<*>?) = block(parent)
    }
}