package com.exozet.android.core.extensions

import android.text.TextWatcher
import android.widget.EditText
import com.exozet.android.core.ui.nullobjects.SimpleTextWatcher


fun EditText.onTextChanged(block: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit): TextWatcher {

    val listener = object : SimpleTextWatcher() {

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            block(s, start, before, count)
        }
    }
    addTextChangedListener(listener)
    return listener
}