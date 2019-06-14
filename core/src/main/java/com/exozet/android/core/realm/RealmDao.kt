package com.exozet.android.core.realm

import io.realm.Realm
import io.realm.RealmModel
import io.realm.kotlin.deleteFromRealm

/**
 * {@inheritDoc}
 */
open class RealmDao(val realm: Realm) {

    /**
     * Gets all [T].
     */
    inline fun <reified T : RealmModel> all() = realm.where(T::class.java).findAllAsync()

    /**
     * Updates and inserts [T].
     */
    inline fun <reified T : RealmModel> insertOrUpdate(item: T) = realm.executeTransactionAsync { it.insertOrUpdate(item) }

    /**
     * Updates and inserts list of [T].
     */
    inline fun <reified T : RealmModel> insertOrUpdate(items: List<T>) = realm.executeTransactionAsync { it.insertOrUpdate(items) }

    /**
     * Deletes [T].
     */
    inline fun <reified T : RealmModel> delete(item: T) = item.deleteFromRealm()

    /**
     * Deletes all [T].
     */
    inline fun <reified T : RealmModel> deleteAll() = realm.executeTransactionAsync { realm ->
        realm.where(T::class.java)
            .findAllAsync()
            .deleteAllFromRealm()
    }
}