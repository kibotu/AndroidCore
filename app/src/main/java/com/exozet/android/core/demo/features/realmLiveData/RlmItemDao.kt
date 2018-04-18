package com.exozet.android.core.demo.features.realmLiveData

import android.arch.lifecycle.LiveData
import com.exozet.android.core.extensions.asLiveData
import io.realm.Realm
import io.realm.RealmResults

class RlmItemDao(val realm: Realm) {

    fun getItems(): LiveData<RealmResults<RlmItem>> {
        return realm.where(RlmItem::class.java).findAllAsync().asLiveData()
    }
}