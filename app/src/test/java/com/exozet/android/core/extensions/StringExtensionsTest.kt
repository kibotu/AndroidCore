package com.exozet.android.core.extensions

import com.exozet.android.core.base.BaseTest
import com.google.common.truth.Truth
import junit.framework.Assert
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils.fromHexString
import org.junit.Test
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.*

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

class StringExtensionsTest : BaseTest() {

    @Test
    fun toJsonTest() {

    }


    @Test
    @Throws(UnsupportedEncodingException::class)
    fun count() {
        Assert.assertEquals(5, "hallo".toByteArray(charset("UTF-8")).size)
        Assert.assertEquals(5, "hallo".length)
        Assert.assertEquals(6, "다렉".toByteArray(charset("UTF-8")).size)
        Assert.assertEquals(2, "다렉".length)
        Assert.assertEquals(9, "우주선".toByteArray(charset("UTF-8")).size)
        Assert.assertEquals(3, "우주선".length)
    }

    @Test
    @Throws(UnsupportedEncodingException::class)
    fun latin() {

        // The input string for this test
        val string = "Hello World"

        // Check length, in characters
        println(string + " Length=" + string.length) // prints "11"

        // Check encoded sizes
        val utf8Bytes = string.toByteArray(charset("UTF-8"))
        println("${java.lang.String(utf8Bytes, "UTF-8")}UTF-8 Size=" + utf8Bytes.size) // prints "11"

        val utf16Bytes = string.toByteArray(charset("UTF-16"))
        println("${java.lang.String(utf16Bytes, "UTF-16")} UTF-16 Size=" + utf16Bytes.size) // prints "24"

        val utf32Bytes = string.toByteArray(charset("UTF-32"))
        println("${java.lang.String(utf32Bytes, "UTF-32")} UTF-32 Size=" + utf32Bytes.size) // prints "44"

        val iso8859Bytes = string.toByteArray(charset("ISO-8859-1"))
        println("${java.lang.String(iso8859Bytes, "ISO-8859-1")} ISO-8859-1 Size=" + iso8859Bytes.size) // prints "11"

        val winBytes = string.toByteArray(charset("CP1252"))
        println("${winBytes.toString(Charset.forName("CP1252"))} CP1252 Size=" + winBytes.size) // prints "11"

        val iso2022Bytes = string.toByteArray(charset("ISO-2022-KR"))
        println("${java.lang.String(iso2022Bytes, "ISO-2022-KR")}ISO-2022-KR Size=" + iso2022Bytes.size) // prints "6"
    }

    @Test
    @Throws(UnsupportedEncodingException::class)
    fun korean() {

        // The input string for this test
        val string = "다렉 우주선"

        // Check length, in characters
        println(string + " Length=" + string.length) // prints "6"

        // Check encoded sizes
        val utf8Bytes = string.toByteArray(charset("UTF-8"))
        println("${java.lang.String(utf8Bytes, "UTF-8")} UTF-8 Size=" + utf8Bytes.size) // prints "16"

        val utf16Bytes = string.toByteArray(charset("UTF-16"))
        println("${java.lang.String(utf16Bytes, "UTF-16")} UTF-16 Size=" + utf16Bytes.size) // prints "14"

        val utf32Bytes = string.toByteArray(charset("UTF-32"))
        println("${java.lang.String(utf32Bytes, "UTF-32")} UTF-32 Size=" + utf32Bytes.size) // prints "24"

        val iso8859Bytes = string.toByteArray(charset("ISO-8859-1"))
        println("${java.lang.String(iso8859Bytes, "ISO-8859-1")} ISO-8859-1 Size=" + iso8859Bytes.size) // prints "6"

        val winBytes = string.toByteArray(charset("CP1252"))
        println("${java.lang.String(winBytes, "CP1252")} CP1252 Size=" + winBytes.size) // prints "6"

        val iso2022Bytes = string.toByteArray(charset("ISO-2022-KR"))
        println("${java.lang.String(iso2022Bytes, "ISO-2022-KR")} ISO-2022-KR Size=" + iso2022Bytes.size) // prints "18"
    }

    @Test
    fun replace() {
        val placeholderText = """For your device to get connected to \"%@\" it needs to have the WIFI password."""

        val expected = "Dalek"

        Assert.assertEquals(
            "For your device to get connected to $expected it needs to have the WIFI password.",
            placeholderText.replace("\\\"%@\\\"", expected)
        )
    }

    @Test
    fun hexStringToByteArray() {

        val hexString = "00112233445566778899AABBCCDDEEFF" //getString(R.string.pubnub_cipher_key);

        val expected =
            byteArrayOf(0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 0x88.toByte(), 0x99.toByte(), 0xaa.toByte(), 0xbb.toByte(), 0xcc.toByte(), 0xdd.toByte(), 0xee.toByte(), 0xff.toByte())

        Assert.assertEquals(Arrays.toString(expected), Arrays.toString(fromHexString(hexString)))
    }

    @Test
    fun sha256() {
        Truth.assertThat("04-73-2F-02-93-C0".sha256())
            .isEqualTo("0413e0cecd75d7bc0a463ed5593962257cfb4117b68c5fb3618ba4085721495e")
    }
}