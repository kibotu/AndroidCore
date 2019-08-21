package com.exozet.android.core.markdown

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import com.exozet.android.core.R
import com.exozet.android.core.base.BaseFragment
import com.exozet.android.core.extensions.onClick
import com.exozet.android.core.extensions.resString
import com.exozet.android.core.storage.bundle
import kotlinx.android.synthetic.main.fragment_raw_output.*
import net.kibotu.logger.Logger.logv


class RawOutputFragment : BaseFragment() {

    override val layout = R.layout.fragment_raw_output

    private var raw by bundle<String?>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        content.movementMethod = ScrollingMovementMethod()

        logv { "$raw" }
        content.text = "$raw"

        share.onClick {
            share()
        }
    }

    private fun share() =
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, raw)
            type = "text/plain"
            startActivity(Intent.createChooser(this, R.string.share_with.resString))
        }
}