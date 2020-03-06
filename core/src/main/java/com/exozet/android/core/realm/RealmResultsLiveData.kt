package com.exozet.android.core.realm

import androidx.lifecycle.LiveData
import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.RealmResults


open class RealmResultsLiveData<T : RealmObject>(protected val results: RealmResults<T>, var valueOnActive: Boolean = false) : LiveData<RealmResults<T>>() {

    protected val listener = RealmChangeListener<RealmResults<T>> { postValue(it.freeze()) }

    override fun onActive() {
        results.addChangeListener(listener)
        if (valueOnActive)
            value = results.freeze()
    }

    override fun onInactive() = results.removeChangeListener(listener)
}