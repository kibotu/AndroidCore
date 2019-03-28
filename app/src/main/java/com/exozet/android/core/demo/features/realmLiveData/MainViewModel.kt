package com.exozet.android.core.demo.features.realmLiveData

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.vicpin.krealmextensions.queryAll
import com.vicpin.krealmextensions.save
import io.realm.Realm
import io.realm.RealmResults

class MainViewModel : ViewModel() {

    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    fun getItems(): LiveData<RealmResults<RlmItem>> = RlmItemDao(realm).getItems()

    fun addItem() {
        RlmItem("Number ${RlmItem().queryAll().size + 1}").save()
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }
}