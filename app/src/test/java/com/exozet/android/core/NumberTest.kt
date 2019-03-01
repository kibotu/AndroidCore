package com.exozet.android.core

import com.exozet.android.core.base.BaseTest
import com.exozet.android.core.extensions.percentToValueOfRange
import com.exozet.android.core.extensions.valueToPercentOfRange
import com.google.common.truth.Truth
import org.junit.Test

class NumberTest : BaseTest() {

    @Test
    fun percentTest() {

        val min = 0f
        val max = 100f

        (0..100).forEach {

            val expected = (it - min) / (max - min)
            Truth.assertThat(it.toFloat().valueToPercentOfRange(min, max)).isEqualTo(expected)
        }

        Truth.assertThat(0f.valueToPercentOfRange(-100f, 100f)).isEqualTo(0.5f)
        Truth.assertThat(0f.valueToPercentOfRange(100f, -100f)).isEqualTo(0.5f)
    }

    @Test
    fun percentValueTest() {

        val min = 0f
        val max = 100f

        (0..100).forEach {

            val expected = min + it * (max - min)
            Truth.assertThat(it.toFloat().percentToValueOfRange(min, max)).isEqualTo(expected)
        }

        Truth.assertThat(0.5f.percentToValueOfRange(-100f, 100f)).isEqualTo(0f)
        Truth.assertThat(0.5f.percentToValueOfRange(100f, -100f)).isEqualTo(0f)
    }
}