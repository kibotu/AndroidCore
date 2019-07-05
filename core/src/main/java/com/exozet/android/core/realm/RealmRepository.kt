package com.exozet.android.core.realm

import androidx.annotation.WorkerThread
import io.realm.RealmModel
import io.realm.RealmResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class RealmRepository<Dao : RealmDao<T>, T : RealmModel> {

    abstract val db: Dao

    /**
     * Gets all [T].
     */
    inline fun <reified T : RealmModel> all(): RealmResults<T> = db.all()

    /**
     * Updates and inserts [T].
     */
    fun insertOrUpdate(item: T) = db.insertOrUpdate(item)

    /**
     * Updates and inserts list of [T].
     */
    fun insertOrUpdate(items: List<T>) = db.insertOrUpdate(items)

    /**
     * Deletes [T].
     */
    fun delete(item: T) = db.delete(item)

    /**
     * Deletes all [T].
     */
    inline fun <reified T : RealmModel> deleteAll() = db.deleteAll<T>()

    /**
     * Loads [T] from network and inserts into [Dao].
     */
    suspend inline fun <reified T> loadAndUpdate() {
        val result = withContext(Dispatchers.IO) {
            load()
        } ?: return

        db.insertOrUpdate(result)
    }

    /**
     * Loads [T] on [Dispatchers.IO] thread.
     */
    @WorkerThread
    open suspend fun load(): T? = TODO()

    /**
     * Loads [T] from network and inserts into [Dao].
     */
    suspend inline fun <reified R : List<T>> loadAndUpdateList() {
        val result = withContext(Dispatchers.IO) {
            loadList()
        } ?: return

        db.insertOrUpdate(result)
    }

    /**
     * Loads a list of [T] on [Dispatchers.IO] thread.
     */
    @WorkerThread
    open suspend fun loadList(): List<T>? = TODO()
}