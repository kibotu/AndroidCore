package com.exozet.android.core.services.crypto

import android.util.Base64
import net.kibotu.logger.Logger
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Blowfish {

    private const val ALGORITHM = "Blowfish"
    private const val MODE = "Blowfish/CBC/PKCS5Padding"
    private const val IV = "abcdefgh"

    /**
     * e.g. class App : MultiDexApplication()  { init { Blowfish.KEY = "mysecurepassword" } }
     */
    var KEY = "changemeplease"

    fun encrypt(value: String) = try {
        val secretKeySpec = SecretKeySpec(KEY.toByteArray(), ALGORITHM)
        val cipher = Cipher.getInstance(MODE)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, IvParameterSpec(IV.toByteArray()))
        val values = cipher.doFinal(value.trimMargin().toByteArray())
        Base64.encodeToString(values, Base64.DEFAULT).trimMargin()
    } catch (e: Exception) {
        Logger.e(e)
        ""
    }

    fun decrypt(value: String?) = try {
        val values = Base64.decode(value, Base64.DEFAULT)
        val secretKeySpec = SecretKeySpec(KEY.toByteArray(), ALGORITHM)
        val cipher = Cipher.getInstance(MODE)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec(IV.toByteArray()))
        String(cipher.doFinal(values)).trimMargin()
    } catch (e: Exception) {
        Logger.e(e)
        ""
    }
}