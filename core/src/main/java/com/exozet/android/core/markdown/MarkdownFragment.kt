package com.exozet.android.core.markdown

import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.view.View
import com.exozet.android.core.R
import com.exozet.android.core.base.BaseFragment
import com.exozet.android.core.storage.bundle
import kotlinx.android.synthetic.main.fragment_markdown.*

class MarkdownFragment : BaseFragment() {

    override val layout = R.layout.fragment_markdown

    var filename by bundle<String?>()

    var url by bundle<String?>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when {
            !isEmpty(filename) -> markdown_view.loadMarkdownFromAssets(filename)
            !isEmpty(url) -> markdown_view.loadUrl(url)
        }

        markdown_view.isOpenUrlInBrowser = true
    }
}