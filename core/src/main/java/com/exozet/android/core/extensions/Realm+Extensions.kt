package com.exozet.android.core.extensions

import androidx.lifecycle.LiveData
import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmResults
import io.realm.kotlin.addChangeListener
import io.realm.kotlin.removeChangeListener

open class RealmResultsLiveData<T : RealmModel>(val results: RealmResults<T>, var valueOnActive: Boolean = false) : LiveData<RealmResults<T>>() {

    protected val listener = RealmChangeListener<RealmResults<T>> { value = it }

    override fun onActive() {
        results.addChangeListener(listener)
        if (valueOnActive)
            value = results
    }

    override fun onInactive() = results.removeChangeListener(listener)
}

open class RealmModelLiveData<T : RealmModel>(val model: T, var valueOnActive: Boolean = false) : LiveData<T>() {

    protected val listener = RealmChangeListener<T> { value = it }

    override fun onActive() {
        model.addChangeListener(listener)
        if (valueOnActive)
            value = model
    }

    override fun onInactive() = model.removeChangeListener(listener)
}

inline fun <reified T : RealmModel> RealmResults<T>.asLiveData(valueOnActive: Boolean = false) = RealmResultsLiveData(this, valueOnActive)

inline fun <reified T : RealmModel> T.asLiveData(valueOnActive: Boolean = false) = RealmModelLiveData(this, valueOnActive)