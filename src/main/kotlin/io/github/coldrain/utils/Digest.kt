package io.github.coldrain.utils

import java.lang.StringBuilder
import java.security.MessageDigest

/**
 * io.github.coldrain.utils.Digest
 * feedback-wall-backend
 *
 * @author 寒雨
 * @since 2022/5/6 21:12
 **/
fun String.md5(): String {
    val instance: MessageDigest = MessageDigest.getInstance("MD5")
    //对字符串加密，返回字节数组
    val digest:ByteArray = instance.digest(toByteArray())
    val sb = StringBuilder()
    for (b in digest) {
        //获取低八位有效值
        val i :Int = b.toInt() and 0xff
        //将整数转化为16进制
        var hexString = Integer.toHexString(i)
        if (hexString.length < 2) {
            //如果是一位的话，补0
            hexString = "0$hexString"
        }
        sb.append(hexString)
    }
    return sb.toString()
}