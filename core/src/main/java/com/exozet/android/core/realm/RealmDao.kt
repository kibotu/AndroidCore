package com.exozet.android.core.realm

import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults
import io.realm.kotlin.deleteFromRealm

/**
 * {@inheritDoc}
 */
open class RealmDao<T : RealmModel>(val realm: Realm) {

    /**
     * Gets all [T].
     */
    inline fun <reified T : RealmModel> all(): RealmResults<T> = realm.where(T::class.java).findAllAsync()

    /**
     * Updates and inserts [T].
     */
    open fun insertOrUpdate(item: T) = realm.executeTransactionAsync { it.insertOrUpdate(item) }

    /**
     * Updates and inserts list of [T].
     */
    open fun insertOrUpdate(items: List<T>) = realm.executeTransactionAsync { it.insertOrUpdate(items) }

    /**
     * Deletes [T].
     */
    open fun delete(item: T) = item.deleteFromRealm()

    /**
     * Deletes all [T].
     */
    inline fun <reified T : RealmModel> deleteAll() = realm.executeTransactionAsync { realm ->
        realm.where(T::class.java)
            .findAllAsync()
            .deleteAllFromRealm()
    }
}