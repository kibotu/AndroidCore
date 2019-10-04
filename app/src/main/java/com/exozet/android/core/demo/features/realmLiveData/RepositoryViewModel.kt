package com.exozet.android.core.demo.features.realmLiveData

import android.content.Context
import androidx.lifecycle.ViewModel
import com.exozet.android.core.demo.BuildConfig
import com.exozet.android.core.extensions.asLiveData
import com.exozet.android.core.realm.RealmDao
import com.exozet.android.core.realm.RealmRepository
import com.github.florent37.application.provider.application
import com.vicpin.krealmextensions.queryAll
import com.vicpin.krealmextensions.save
import io.realm.*
import net.kibotu.logger.Logger.logv
import java.io.Closeable

class RepositoryViewModel : ViewModel() {

    private val database by lazy { Database(application!!) }

    private val stringRepository by lazy { StringRepository(database) }

    val strings by lazy { stringRepository.db.all<StringModel>().asLiveData() }

    fun addItem() = StringModel("Number ${StringModel().queryAll().size + 1}").save()

    override fun onCleared() {
        database.close()
        super.onCleared()
    }

    fun deleteAll() = database.deleteAll()
}

open class StringModel(var value: String? = null) : RealmObject()

class StringDao(realm: Realm) : RealmDao<RealmModel>(realm)

class StringRepository(database: Database) : RealmRepository<StringDao, RealmModel>() {

    override val db: StringDao = database.stringDao
}

/**
 * Manages local storage.
 */
class Database(context: Context) : Closeable {

    /**
     * Realm instance for DAOs. Stays open so we can listen to stream updates.
     */
    private val realm by lazy { Realm.getDefaultInstance() }

    init {
        Realm.init(context.applicationContext)
        Realm.setDefaultConfiguration(
            RealmConfiguration
                .Builder()
                .schemaVersion(BuildConfig.VERSION_CODE.toLong())
                .deleteRealmIfMigrationNeeded()
                .migration(Migration())
                .name("testcore.realm")
                .build()
        )
    }

    val stringDao by lazy { StringDao(realm) }

    override fun close() {
        if (!realm.isClosed)
            realm.close()
    }

    fun deleteAll() = Realm.getDefaultInstance().use { realm ->
        realm.executeTransaction {
            it.deleteAll()
        }
    }

    // region migration

    class Migration : RealmMigration {

        override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
            logv { "migrate $oldVersion => $newVersion" }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }

    }

    // endregion
}