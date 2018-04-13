@file:JvmName("TextViewExtensions")

package com.exozet.android.core.extensions

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.N
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
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

fun TextView.bindSpannableText(text: String, start: Int, end: Int, action: () -> Unit) {
    val spannable = SpannableString(text)

    val clickableSpan = object : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint?) {
            ds?.color = ContextCompat.getColor(context, android.R.color.holo_blue_light)
            ds?.isUnderlineText = false
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
        override fun updateDrawState(ds: TextPaint?) {
            ds?.color = ContextCompat.getColor(context, android.R.color.holo_blue_light)
            ds?.isUnderlineText = false
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
