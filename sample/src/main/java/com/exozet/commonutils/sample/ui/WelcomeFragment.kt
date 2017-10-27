package com.exozet.commonutils.sample.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import com.exozet.androidcommonutils.sample.ui.BaseFragment
import com.exozet.commonutils.sample.R
import com.exozet.commonutils.sample.misc.MainActivityListener

/**
 * Created by armando.shkurti on 27/10/17.
 */
class WelcomeFragment : BaseFragment() {

    override val layout: Int = R.layout.fragment_welcome

    private var activityListener: MainActivityListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)

        activityListener = context as? MainActivity
    }


    companion object {

        val TAG = WelcomeFragment::class.java.simpleName

        fun newInstance(): WelcomeFragment {
            return WelcomeFragment()
        }
    }
}