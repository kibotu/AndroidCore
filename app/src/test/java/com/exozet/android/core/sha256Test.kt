package com.exozet.android.core

import com.exozet.android.core.base.BaseTest
import com.exozet.android.core.extensions.sha256_HMAC
import com.google.common.truth.Truth
import org.junit.Test


class sha : BaseTest() {
    /**
     * https://tools.ietf.org/html/rfc7636#appendix-A
     */
    @Test
    fun sha256_HMACTest() {
        Truth.assertThat("any carnal pleasure.".sha256_HMAC()).isEqualTo("klV7mnDnMorgaiIdbLmVSjsQDWP3UpClQSSnaI2nOXE=")
    }

    @Test
    fun retrievalTokenEncryptionTest() {
        val actual = "any carnal pleasure.".sha256_HMAC()

        val expected = "klV7mnDnMorgaiIdbLmVSjsQDWP3UpClQSSnaI2nOXE="
        Truth.assertThat(actual).isEqualTo(expected)

        val expected2 = "klV7mnDnMorgaiIdbLmVSjsQDWP3UpClQSSnaI2nOXE"
        val removedCharacters = actual.replace("+", "-")
            .replace("/", "_")
            .replace("=", "")
        Truth.assertThat(removedCharacters).isEqualTo(expected2)
    }
}