package com.exozet.android.core.realm

open class RealmModelLiveData<T : RealmModel>(val model: T, var valueOnActive: Boolean = false) : LiveData<T>() {

    protected val listener = RealmChangeListener<T> { value = it }

    override fun onActive() {
        model.addChangeListener(listener)
        if (valueOnActive)
            value = model
    }

    override fun onInactive() = model.removeChangeListener(listener)
}