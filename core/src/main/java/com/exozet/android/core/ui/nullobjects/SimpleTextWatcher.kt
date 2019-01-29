package com.exozet.android.core.ui.nullobjects

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

open class SimpleTextWatcher : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
    }
}

fun EditText.beforeTextChanged(block: (s: CharSequence?, start: Int, count: Int, after: Int) -> Unit): TextWatcher {
    val listener = object : SimpleTextWatcher() {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            if (s?.isNotEmpty() == true)
                block(s, start, count, after)
        }
    }
    addTextChangedListener(listener)
    return listener
}

fun EditText.onTextChanged(block: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit): TextWatcher {

    val listener = object : SimpleTextWatcher() {

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s?.isNotEmpty() == true)
                block(s, start, before, count)
        }
    }
    addTextChangedListener(listener)
    return listener
}

fun EditText.afterTextChanged(block: (s: CharSequence?) -> Unit): TextWatcher {
    val listener = object : SimpleTextWatcher() {

        override fun afterTextChanged(s: Editable?) {
            if (s?.isNotEmpty() == true)
                block(s)
        }
    }
    addTextChangedListener(listener)
    return listener
}