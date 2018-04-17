package com.exozet.android.core.extensions

import com.exozet.android.core.storage.RealmLiveData
import io.realm.RealmModel
import io.realm.RealmResults

/**
 * Created by Tim Wienrich
 */

fun <T: RealmModel> RealmResults<T>.asLiveData() = RealmLiveData<T>(this)