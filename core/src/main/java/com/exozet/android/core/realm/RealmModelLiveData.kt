package com.exozet.android.core.realm

import androidx.lifecycle.LiveData
import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.kotlin.addChangeListener
import io.realm.kotlin.removeChangeListener

open class RealmModelLiveData<T : RealmModel>(val model: T, var valueOnActive: Boolean = false) : LiveData<T>() {

    protected val listener = RealmChangeListener<T> { value = it }

    override fun onActive() {
        model.addChangeListener(listener)
        if (valueOnActive)
            value = model
    }

    override fun onInactive() = model.removeChangeListener(listener)
}