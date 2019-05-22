@file:JvmName("ViewExtensions")

package com.exozet.android.core.extensions

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.JELLY_BEAN
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.math.MathUtils
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.ViewPager
import com.dtx12.android_animations_actions.actions.Actions.*
import com.dtx12.android_animations_actions.actions.Interpolations
import com.exozet.android.core.utils.MathExtensions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import net.kibotu.ContextHelper

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

fun View.setMargins(
    left: Int? = null,
    top: Int? = null,
    right: Int? = null,
    bottom: Int? = null
) {
    val lp = layoutParams as? ViewGroup.MarginLayoutParams
        ?: return

    lp.setMargins(
        left ?: lp.leftMargin,
        top ?: lp.topMargin,
        right ?: lp.rightMargin,
        bottom ?: lp.bottomMargin
    )

    layoutParams = lp
}

fun View.setPadding(
    left: Int? = null,
    top: Int? = null,
    right: Int? = null,
    bottom: Int? = null
) = setPadding(
    left ?: paddingLeft,
    top ?: paddingTop,
    right ?: paddingRight,
    bottom ?: paddingBottom
)


fun View.setDimension(
    width: Int? = null,
    height: Int? = null
) {
    val params = layoutParams
    params.width = width ?: params.width
    params.height = height ?: params.height
    layoutParams = params
}

fun View?.show(isShowing: Boolean = true) {
    this?.visibility = if (isShowing) View.VISIBLE else View.INVISIBLE
}

fun View?.hide(isHiding: Boolean = true) {
    this?.visibility = if (isHiding) View.INVISIBLE else View.VISIBLE
}

fun View?.gone(isGone: Boolean = true) {
    this?.visibility = if (isGone) View.GONE else View.VISIBLE
}

fun gone(isGone: Boolean = true, vararg views: View) {
    views.forEach { it.gone(isGone) }
}

fun Array<View>.gone(isGone: Boolean = true) {
    forEach { it.gone(isGone) }
}

infix fun View.onClick(function: () -> Unit) {
    setOnClickListener { function() }
}

fun ProgressBar.indeterminateDrawableColor(@ColorRes color: Int) {
    indeterminateDrawable.setColorFilter(
        ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.SRC_IN
    )
}

fun View.aspect(ratio: Float = 3 / 4f) =
    post {
        val params = layoutParams
        params.height = (width / ratio).toInt()
        layoutParams = params
    }


fun View.waitForLayout(onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            else {
                @Suppress("DEPRECATION")
                viewTreeObserver.removeGlobalOnLayoutListener(this)
            }

            onGlobalLayoutListener.onGlobalLayout()
        }
    })
}

fun View.setOnTouchActionUpListener(action: (v: View, event: MotionEvent) -> Boolean) {
    setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> true
            MotionEvent.ACTION_UP -> {
                action(v, event)
            }
            else -> false
        }
    }
}

/**
 * gradient(200f, 0x80C24641.toInt(), 0x80FFFFFF.toInt())
 */
fun View.gradient(radius: Float, vararg colors: Int) {
    if (SDK_INT >= JELLY_BEAN)
        background = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors).apply { cornerRadius = radius }
    else
        throw UnsupportedOperationException("requires SDK >= 16 but was $SDK_INT")
}

fun View.locationInWindow(): Rect {
    val rect = Rect()
    val location = IntArray(2)

    getLocationInWindow(location)

    rect.left = location[0]
    rect.top = location[1]
    rect.right = location[0] + width
    rect.bottom = location[1] + height

    return rect
}

fun View.locationOnScreen(): Rect {
    val rect = Rect()
    val location = IntArray(2)

    getLocationOnScreen(location)

    rect.left = location[0]
    rect.top = location[1]
    rect.right = location[0] + width
    rect.bottom = location[1] + height

    return rect
}

@ColorInt
fun View.getCurrentColor(@ColorInt default: Int = Color.TRANSPARENT): Int = (background as? ColorDrawable)?.color
    ?: default

fun View.resize(width: Float? = null, height: Float? = null) {
    width?.let { layoutParams.width = it.toInt() }
    height?.let { layoutParams.height = it.toInt() }
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


fun View.onBackPressed(block: () -> Boolean) = setOnKeyListener { _, keyCode, _ ->
    when (keyCode) {
        KeyEvent.KEYCODE_BACK -> block()
        else -> false
    }
}

fun ViewPager.scroll(pages: Int) {
    currentItem = MathUtils.clamp(currentItem + pages, 0, childCount)
}

fun EditText.selectEnd() {
    if (!isFocused)
        return

    post {
        setSelection(text.toString().length)
    }
}

infix fun EditText.isEqualTrimmed(other: EditText): Boolean = text.toString().trim() == other.text.toString().trim()


fun TabLayout.addTab(@StringRes title: Int, @DrawableRes icon: Int, @LayoutRes customView: Int) {
    val tab = LayoutInflater.from(context).inflate(customView, this as ViewGroup, false) as TextView
    tab.setText(title)
    tab.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
    addTab(newTab().setCustomView(tab))
}

fun TabLayout.updateTabAt(position: Int, @StringRes title: Int, @DrawableRes icon: Int, @LayoutRes customView: Int) {
    val tab = LayoutInflater.from(context).inflate(customView, this as ViewGroup, false) as TextView
    tab.setText(title)
    tab.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
    getTabAt(position)?.customView = tab
}

fun TabLayout.Tabs(): List<TabLayout.Tab> {

    val tabs = mutableListOf<TabLayout.Tab>()

    (0..tabCount).forEach { index: Int ->
        getTabAt(index)?.let { tabs.add(it) }
    }

    return tabs
}

fun TextInputLayout.setTextInputLayoutUpperHintColor(@ColorInt color: Int) {
    defaultHintTextColor = ColorStateList(arrayOf(intArrayOf()), intArrayOf(color))
}

fun TextInputLayout.toggleTextHintColorOnEmpty(@ColorRes active: Int, @ColorRes inactive: Int) = setTextInputLayoutUpperHintColor(
    if (editText?.text?.isNotEmpty() == true)
        active.resColor else
        inactive.resColor
)

fun ViewGroup.inflate(layoutRes: Int): View = LayoutInflater.from(context).inflate(layoutRes, this, false)


fun View.onLayoutChange(block: (() -> Unit)?) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onLayoutChange(v: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
            v.removeOnLayoutChangeListener(this)
            block?.invoke()
        }
    })
}

fun View.playWobble(influence: Float = 0.01f): Animator = forever(
    sequence(
        scaleTo(1f + influence, 1f - +influence, MathExtensions.randomRange(1f, 1.3f), Interpolations.SineEaseInOut),
        scaleTo(1f - +influence, 1f + influence, MathExtensions.randomRange(1f, 1.3f), Interpolations.SineEaseInOut)
    )
).apply {
    play(this, this@playWobble)
}

fun View.waitForLayout(block: (() -> Unit)?) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            else {
                @Suppress("DEPRECATION")
                viewTreeObserver.removeGlobalOnLayoutListener(this)
            }

            block?.invoke()
        }
    })
}

val DialogFragment?.isShowing
    get() = this != null && dialog != null && dialog?.isShowing == true && !isRemoving

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

val TextView.textTrimmed
    get() = text.toString().trimMargin()


fun Array<View?>?.hideOnLostFocus(event: MotionEvent) {
    if (this == null)
        return

    var hit = false

    for (view in this)
        hit = hit or (view?.screenLocation?.contains(event.x.toInt(), event.y.toInt()) ?: false)

    if (event.action == MotionEvent.ACTION_DOWN && !hit)
        hideKeyboard()
}

val View.screenLocation: Rect
    get() {
        val location = IntArray(2)
        getLocationOnScreen(location)
        return Rect().apply {
            left = location[0]
            top = location[1]
            right = location[0] + width
            bottom = location[1] + height
        }
    }

fun View.hideKeyboard() = (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(windowToken, 0)

fun Activity.hideKeyboard() = contentRootView.hideKeyboard()

fun hideKeyboard() = ContextHelper.getAppCompatActivity()?.hideKeyboard()

val argbEvaluator by lazy { ArgbEvaluator() }

fun ImageView.setColorFilterWithOffset(@FloatRange(from = 0.0, to = 1.0) percent: Float, @ColorInt from: Int, @ColorInt to: Int) {
    setColorFilter(argbEvaluator.evaluate(percent, from, to) as Int, PorterDuff.Mode.SRC_ATOP)
}


fun AppBarLayout.onCollapseStateChanged(@FloatRange(from = 0.0, to = 1.0) max: Float = 0.75f): Observable<Boolean> {

    var listener: AppBarLayout.OnOffsetChangedListener? = null

    return BehaviorSubject.create<Boolean> { emitter ->

        if (emitter.isDisposed)
            return@create

        listener = AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val isCollapsed = Math.abs(verticalOffset) - appBarLayout.totalScrollRange == 0
            val percentage = Math.abs(verticalOffset) / appBarLayout.totalScrollRange.toFloat()
            // logv("percentage=$percentage isCollapsed=$isCollapsed")
            emitter.onNext(percentage >= max)
        }

        addOnOffsetChangedListener(listener)

    }.doOnDispose {
        removeOnOffsetChangedListener(listener)
    }.distinctUntilChanged()
}

fun AppBarLayout.onOffsetChanged(): Observable<Float> {

    var listener: AppBarLayout.OnOffsetChangedListener? = null

    return BehaviorSubject.create<Float> { emitter ->

        if (emitter.isDisposed)
            return@create

        listener = AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            emitter.onNext(Math.abs(verticalOffset) / appBarLayout.totalScrollRange.toFloat())
        }

        addOnOffsetChangedListener(listener)

    }.doOnDispose {
        removeOnOffsetChangedListener(listener)
    }.distinctUntilChanged()
}
