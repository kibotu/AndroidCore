@file:JvmName("ViewModelExtensions")

package com.exozet.android.core.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import net.kibotu.ContextHelper


inline fun <reified T : ViewModel> Class<T>.viewModel() = ViewModelProviders.of(ContextHelper.getAppCompatActivity()!!).get(this)