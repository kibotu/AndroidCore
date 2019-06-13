package com.exozet.android.core.extensions

import androidx.lifecycle.LiveData
import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmResults
import io.realm.kotlin.addChangeListener
import io.realm.kotlin.removeChangeListener

open class RealmResultsLiveData<T : RealmModel>(val results: RealmResults<T>) : LiveData<RealmResults<T>>() {

    protected val listener = RealmChangeListener<RealmResults<T>> { value = it }

    override fun onActive() {
        results.addChangeListener(listener)
        value = results
    }

    override fun onInactive() = results.removeChangeListener(listener)
}

open class RealmModelLiveData<T : RealmModel>(val model: T) : LiveData<T>() {

    protected val listener = RealmChangeListener<T> { value = it }

    override fun onActive() {
        model.addChangeListener(listener)
        value = model
    }

    override fun onInactive() = model.removeChangeListener(listener)
}

inline fun <reified T : RealmModel> RealmResults<T>.asLiveData() = RealmResultsLiveData(this)

inline fun <reified T : RealmModel> T.asLiveData() = RealmModelLiveData(this)