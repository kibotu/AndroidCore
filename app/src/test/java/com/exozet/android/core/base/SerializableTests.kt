package com.exozet.android.core.base

import com.exozet.android.core.gson.gson
import com.exozet.android.core.gson.toJsonPrettyPrinting
import net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals
import org.junit.Assert
import org.junit.Test

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

abstract class SerializableTests(var klass: Class<*>) : BaseTest() {

    open val expectedJson: String = ""

    @Test
    open fun serialize() {
        val actual = gson.fromJson(expectedJson.trimMargin(), klass)
        Assert.assertNotNull(actual)
    }

    @Test
    open fun deserialize() {
        val actual = gson.fromJson(expectedJson.trimMargin(), klass)
        Assert.assertNotNull(actual)

        val actualJson = actual.toJsonPrettyPrinting()
        assertJsonEquals(expectedJson.trimMargin(), actualJson)
    }
}