package com.exozet.android.core.realm

open class RealmResultsLiveData<T : RealmModel>(val results: RealmResults<T>, var valueOnActive: Boolean = false) : LiveData<RealmResults<T>>() {

    protected val listener = RealmChangeListener<RealmResults<T>> { value = it }

    override fun onActive() {
        results.addChangeListener(listener)
        if (valueOnActive)
            value = results
    }

    override fun onInactive() = results.removeChangeListener(listener)
}