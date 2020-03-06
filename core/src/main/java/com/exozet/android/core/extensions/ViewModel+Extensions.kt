@file:JvmName("ViewModelExtensions")

package com.exozet.android.core.extensions

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.github.florent37.application.provider.ActivityProvider


/**
 * Try using `by viewModels` if possible.
 */
@MainThread
inline fun <reified VM : ViewModel> viewModel(viewModelStoreOwner: ViewModelStoreOwner = ActivityProvider.currentActivity as ViewModelStoreOwner) = lazy { ViewModelProvider(viewModelStoreOwner).get(VM::class.java) }