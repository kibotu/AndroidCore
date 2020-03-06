package com.exozet.android.core.extensions

import com.exozet.android.core.realm.RealmDao
import com.exozet.android.core.realm.RealmModelLiveData
import com.exozet.android.core.realm.RealmResultsLiveData
import io.realm.RealmList
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.RealmResults

inline fun <reified T : RealmObject> RealmResults<T>.asLiveData(valueOnActive: Boolean = false) = RealmResultsLiveData(this, valueOnActive)

inline fun <reified T : RealmObject> T.asLiveData(valueOnActive: Boolean = false) = RealmModelLiveData(this, valueOnActive)

inline fun <reified T : RealmObject> T.detachRealm(): T = realm?.copyFromRealm(this) ?: this

/**
 * https://github.com/vicpinm/Kotlin-Realm-Extensions/blob/master/library-base/src/main/java/com/vicpin/krealmextensions/RealmExtensions.kt#L275
 */
inline fun <reified T : RealmObject> T.update(crossinline block: T.(t: T) -> Unit) {
    realm.executeTransaction {
        block(this)
    }
}

/**
 * Deletes all [T].
 */
inline fun <reified T : RealmModel> RealmDao<T>.deleteAllSync() = realm.executeTransaction { realm ->
    realm.where(T::class.java)
        .findAll()
        .deleteAllFromRealm()
}

/**
 * Inserts or updates all [T].
 */
inline fun <reified T : RealmModel> RealmDao<T>.insertOrUpdateSync(t: T) = realm.executeTransaction { realm -> realm.insertOrUpdate(t) }

/**
 * Inserts or updates all [T].
 */
inline fun <reified T : RealmModel> RealmDao<T>.insertOrUpdateSync(t: List<T>) = realm.executeTransaction { realm -> realm.insertOrUpdate(t) }

/**
 * Deletes all [T].
 */
inline fun <reified T : RealmModel> RealmDao<T>.allSync(): RealmResults<T> = realm.where(T::class.java).findAll()

inline fun <reified T : RealmObject> RealmDao<T>.isEmpty() = realm.where(T::class.java).findFirst() == null

inline fun <reified E : RealmObject> List<E>.toRealmList(): RealmList<E>? {
    val list = RealmList<E>()
    list.addAll(this)
    return list
}
