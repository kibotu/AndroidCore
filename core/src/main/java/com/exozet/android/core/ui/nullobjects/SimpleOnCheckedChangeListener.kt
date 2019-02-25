package com.exozet.android.core.ui.nullobjects

import android.widget.CompoundButton

open class SimpleOnCheckedChangeListener : CompoundButton.OnCheckedChangeListener {
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
    }
}

inline fun CompoundButton.onCheckedChanged(crossinline block: (Boolean) -> Unit) {
    setOnCheckedChangeListener(object : SimpleOnCheckedChangeListener() {
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) = block(isChecked)
    })
}