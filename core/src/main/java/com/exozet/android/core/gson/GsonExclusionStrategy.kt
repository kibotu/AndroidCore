package com.exozet.android.core.gson

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes

/**
 * https://www.baeldung.com/gson-exclude-fields-serialization
 */
class GsonExclusionStrategy : ExclusionStrategy {

    override fun shouldSkipClass(clazz: Class<*>): Boolean = false

    override fun shouldSkipField(field: FieldAttributes): Boolean = field.getAnnotation<Exclude>(Exclude::class.java) != null
}