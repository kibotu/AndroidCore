package com.exozet.android.core.realm

import com.exozet.android.core.interfaces.Repository
import io.realm.RealmModel

/**
 * {@inheritDoc}
 */
interface RealmRepository<T : RealmModel> : Repository<T>