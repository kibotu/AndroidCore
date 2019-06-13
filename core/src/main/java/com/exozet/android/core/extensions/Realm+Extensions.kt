package com.exozet.android.core.extensions

import com.exozet.android.core.realm.RealmLiveData
import io.realm.RealmModel
import io.realm.RealmResults

/**
 * Created by Tim Wienrich
 */

inline fun <reified T : RealmModel> RealmResults<T>.asLiveData() = RealmLiveData(this)