package com.exozet.android.core.realm

import androidx.lifecycle.LiveData
import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.kotlin.addChangeListener
import io.realm.kotlin.removeChangeListener

open class RealmModelLiveData<T : RealmObject>(val model: T, var valueOnActive: Boolean = false) : LiveData<T>() {

    protected val listener = RealmChangeListener<T> { postValue(it.freeze()) }

    override fun onActive() {
        model.addChangeListener(listener)
        if (valueOnActive)
            value = model
    }

    override fun onInactive() = model.removeChangeListener(listener)
}