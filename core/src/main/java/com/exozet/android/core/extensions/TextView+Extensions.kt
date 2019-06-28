@file:JvmName("TextViewExtensions")

package com.exozet.android.core.extensions

import android.content.res.ColorStateList
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.N
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.google.android.material.textfield.TextInputLayout
import net.kibotu.ContextHelper

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

var TextView.html: String?
    get() = text?.toString()
    set(value) {
        text = if (SDK_INT >= N) {
            Html.fromHtml(value, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(value)
        }
    }

var TextView.content: String
    get() = text.trim().toString()
    set(value) {
        text = value.trim()
    }

fun TextView.bindSpannableText(text: String, start: Int, end: Int, action: () -> Unit) {

    val spannable = SpannableString(text)

    val clickableSpan = object : ClickableSpan() {

        override fun updateDrawState(ds: TextPaint) {

            ds.color = ContextCompat.getColor(context, android.R.color.holo_blue_light)
            ds.isUnderlineText = false
        }

        override fun onClick(view: View) {
            action()
        }
    }

    spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

    movementMethod = LinkMovementMethod.getInstance()
    setText(spannable)
}

fun TextView.bindSpannableText(text: String, linkableWord: String, action: () -> Unit) {

    val spannable = SpannableString(text)

    val clickableSpan = object : ClickableSpan() {

        override fun updateDrawState(ds: TextPaint) {

            ds.color = ContextCompat.getColor(context, android.R.color.holo_blue_light)
            ds.isUnderlineText = false
        }

        override fun onClick(view: View) {
            action()
        }
    }

    val start = text.indexOf(linkableWord)
    val end = start + linkableWord.length

    spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

    movementMethod = LinkMovementMethod.getInstance()
    setText(spannable)
}

fun TextView.bold() = setTypeface(typeface, Typeface.DEFAULT_BOLD.style)

fun TextView.setTextColorRes(@ColorRes color: Int) {
    setTextColor(ContextCompat.getColor(ContextHelper.getApplication()!!, color))
}

fun TextView.getTrimmedText(): String = text.toString().trim { it <= ' ' }

fun TextView.getTrimmedHint(): String = hint.toString().trim { it <= ' ' }

fun TextView.underline() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

fun TextView.capitalize() {
    text = text.toString().trim().toLowerCase().capitalize()
}

fun TextView.toArabicNumbers() {
    text = text.toString().asArabicNumbers()
}

fun TextView.addAfterTextChangedListener(block: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            block(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}

val TextView.isNotEmpty get() = textTrimmed.isNotEmpty()

val TextView.isEmpty get() = textTrimmed.isEmpty()

var TextView.textOrGone
    get() = text
    set(value) {
        text = value
        if (value.isNullOrEmpty())
            gone()
    }

fun TextView.setTextWithViewsOrGone(value: String?, vararg views: TextView?, block: (String) -> String) = if (value.isNotNullOrEmpty()) {
    text = block(value)
} else {
    isGone = true
    views.forEach { it?.isGone = true }
}

inline fun <reified T : Number> TextView.setTextWithViewsOrGone(value: T?, vararg views: TextView?, block: (T) -> String) = if (value.isNotNullOrZero()) {
    text = block(value)
} else {
    isGone = true
    views.forEach { it?.isGone = true }
}

fun EditText.onImeActionDone(block: () -> Unit) = setOnEditorActionListener { _, actionId, _ ->
    when (actionId) {
        EditorInfo.IME_ACTION_DONE -> {
            block()
            return@setOnEditorActionListener true
        }
        else -> return@setOnEditorActionListener false
    }
}

class ClickSpan(val listener: View.OnClickListener?) : ClickableSpan() {

    override fun onClick(widget: View) {
        listener?.onClick(widget)
    }
}

fun TextView.clickify(clickableText: String, listener: View.OnClickListener) {
    val text = text
    val string = text.toString()
    val span = ClickSpan(listener)

    val start = string.indexOf(clickableText)
    val end = start + clickableText.length
    if (start == -1) return

    if (text is Spannable) {
        text.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    } else {
        val s = SpannableString.valueOf(text)
        s.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        setText(s)
    }

    val m = movementMethod
    if (m == null || m !is LinkMovementMethod) {
        movementMethod = LinkMovementMethod.getInstance()
    }
}

infix fun EditText.isEqualTrimmed(other: EditText): Boolean = text.toString().trim() == other.text.toString().trim()

fun EditText.selectEnd() {
    if (!isFocused)
        return

    post {
        setSelection(text.toString().length)
    }
}


fun TextInputLayout.setTextInputLayoutUpperHintColor(@ColorInt color: Int) {
    defaultHintTextColor = ColorStateList(arrayOf(intArrayOf()), intArrayOf(color))
}

fun TextInputLayout.toggleTextHintColorOnEmpty(@ColorRes active: Int, @ColorRes inactive: Int) = setTextInputLayoutUpperHintColor(
    if (editText?.text?.isNotEmpty() == true)
        active.resColor else
        inactive.resColor
)

val TextView.textTrimmed
    get() = text.toString().trimMargin()
