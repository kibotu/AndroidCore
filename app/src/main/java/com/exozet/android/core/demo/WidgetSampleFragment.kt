package com.exozet.android.core.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.exozet.android.core.base.BaseFragment
import com.exozet.android.core.extensions.indeterminateDrawableColor
import kotlinx.android.synthetic.main.fragment_widget_sample.*
import kotlinx.android.synthetic.main.layout_toolbar.*

/**
 * Created by armando.shkurti on 04.04.18.
 */
class WidgetSampleFragment : BaseFragment() {

    override val layout: Int = R.layout.fragment_widget_sample

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.title = "Sample"
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

        progressBar.indeterminateDrawableColor(R.color.white)
    }

    companion object {
        fun newInstance(): WidgetSampleFragment {
            return WidgetSampleFragment()
        }
    }
}