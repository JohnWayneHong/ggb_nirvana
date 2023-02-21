package com.ggb.nirvanaclub.utils

import android.util.Base64
import java.security.KeyFactory
import java.security.KeyFactory.*
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

open class RsaEncryptionUtils {
    /**
     * RSA 加密
     * @param str 加密的密文
     * publicKey 密钥
     */
    open fun rsaEncode(str: String): String {
        val publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDExHT9O7vMpnIQdGeVevncirAY\n" +
                "RZYdyEOibH7IYWT8y/g7xaQmBUfdUtJMslf6NdkLssTFVsqJ+LzHWPpcMoTVPhp3\n" +
                "6XvJv/PGVnTsy9PQuCOq543JsC0waEAVBhs1XFZvgg1YFNcv36CjSt2ghrskREiP\n" +
                "QbjsupcBUITQ9NSybQIDAQAB"
        var outStr = ""
        try {
            // base64编码的公钥
            val decoded: ByteArray = Base64.decode(publicKey, Base64.DEFAULT)
            val pubKey = getInstance("RSA")
                .generatePublic(X509EncodedKeySpec(decoded)) as RSAPublicKey
            // RSA加密
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.ENCRYPT_MODE, pubKey)
            outStr = Base64.encodeToString(
                cipher.doFinal(str.toByteArray(charset("UTF-8"))),
                Base64.DEFAULT
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return outStr
    }

}



