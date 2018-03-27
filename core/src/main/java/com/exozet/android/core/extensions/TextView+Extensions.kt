@file:JvmName("TextViewExtensions")

package com.exozet.android.core.extensions

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.N
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView

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