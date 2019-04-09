package com.exozet.android.core.demo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.exozet.android.core.base.BaseFragment
import com.exozet.android.core.extensions.indeterminateDrawableColor
import com.exozet.android.core.extensions.onClick
import kotlinx.android.synthetic.main.fragment_widget_sample.*
import kotlinx.android.synthetic.main.layout_toolbar.*

/**
 * Created by armando.shkurti on 04.04.18.
 */
class WidgetSampleFragment : BaseFragment() {

    override val layout = R.layout.fragment_widget_sample

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.title = "Sample"
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

        progressBar.indeterminateDrawableColor(R.color.white)

        customToolbarBack onClick onCustomBack()
    }

    private fun onCustomBack() = {
        showSnackBar("Back")
    }
}