package com.exozet.android.core.extensions

import com.exozet.android.core.realm.RealmModelLiveData
import com.exozet.android.core.realm.RealmResultsLiveData
import io.realm.RealmModel
import io.realm.RealmResults


inline fun <reified T : RealmModel> RealmResults<T>.asLiveData(valueOnActive: Boolean = false) = RealmResultsLiveData(this, valueOnActive)

inline fun <reified T : RealmModel> T.asLiveData(valueOnActive: Boolean = false) = RealmModelLiveData(this, valueOnActive)