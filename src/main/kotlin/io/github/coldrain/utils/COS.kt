package io.github.coldrain.utils

import com.qcloud.cos.COSClient
import com.qcloud.cos.ClientConfig
import com.qcloud.cos.auth.BasicCOSCredentials
import com.qcloud.cos.http.HttpProtocol
import com.qcloud.cos.region.Region

/**
 * io.github.coldrain.utils.COS
 * feedback-wall-backend
 *
 * @author 寒雨
 * @since 2022/5/8 0:55
 **/
private val SECRET_ID by lazy { Secret.instance.secretId }
private val SECRET_KEY by lazy { Secret.instance.secretKey }
private val COS_REGION by lazy { Secret.instance.cosRegion }

private fun initClient(): COSClient {
    val cred = BasicCOSCredentials(SECRET_ID, SECRET_KEY)
    val region = Region(COS_REGION)
    val config = ClientConfig(region)
    config.httpProtocol = HttpProtocol.https
    return COSClient(cred, config)
}

val cosClient by lazy { initClient() }
val BUCKET by lazy { Secret.instance.bucket }