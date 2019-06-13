package com.exozet.android.core.realm

import androidx.lifecycle.LiveData
import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmResults

open class RealmLiveData<T : RealmModel>(val results: RealmResults<T>) : LiveData<RealmResults<T>>() {

    protected val listener = RealmChangeListener<RealmResults<T>> { value = it }

    override fun onActive() {
        results.addChangeListener(listener)
        value = results
    }

    override fun onInactive() = results.removeChangeListener(listener)
}