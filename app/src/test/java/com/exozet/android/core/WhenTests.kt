package com.exozet.android.core

import com.exozet.android.core.base.BaseTest
import org.junit.Assert
import org.junit.Test

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

class WhenTests : BaseTest() {

    @Test
    fun whenTestTrueTrue() {

        val a = true
        val b = true

        when {
            a and b -> Assert.assertTrue(true)
            a -> Assert.fail()
            else -> Assert.fail()
        }

        when {
            a -> Assert.assertTrue(true)
            a and b -> Assert.fail()
            else -> Assert.fail()
        }
    }

    @Test
    fun whenTestTrueFalse() {

        val a = true
        val b = false

        when {
            a and b -> Assert.fail()
            a -> Assert.assertTrue(true)
            else -> Assert.fail()
        }
    }

    @Test
    fun whenTestFalseFalse() {

        val a = false
        val b = false

        when {
            a and b -> Assert.fail()
            a -> Assert.fail()
            else -> Assert.assertTrue(true)
        }
    }

    @Test
    fun whenTestFalseTrue() {

        val a = false
        val b = true

        when {
            a and b -> Assert.fail()
            a -> Assert.fail()
            else -> Assert.assertTrue(true)
        }
    }
}
