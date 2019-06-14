package com.exozet.android.core.interfaces

import androidx.lifecycle.LiveData

/**
 * Answers where to get data.
 */
interface Repository<T> {

    /**
     * Gets all [T].
     */
    fun all(): LiveData<List<T>>

    /**
     * Gets [T] by id.
     */
    fun getById(id: String): LiveData<T>

    /**
     * Deletes [T].
     */
    fun delete(item: T)

    /**
     * Updates and inserts [T].
     */
    fun insertOrUpdate(item: T)

    /**
     * Updates and inserts list of [T].
     */
    fun insertOrUpdate(items: List<T>)

    /**
     * Loads and updates.
     */
    suspend fun loadAndUpdate()
}