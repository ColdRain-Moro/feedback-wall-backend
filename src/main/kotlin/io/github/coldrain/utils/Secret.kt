package io.github.coldrain.utils

/**
 * io.github.coldrain.utils.Secret
 * feedback-wall-backend
 *
 * @author 寒雨
 * @since 2022/5/7 1:02
 **/
data class Secret(
    val email: String,
    val password: String,
    val secretId: String,
    val secretKey: String,
    val cosRegion: String,
    val bucket: String
    ) {
    companion object {
        val instance: Secret by lazy { gson.fromJson(Secret::class.java.classLoader.getResource("secret.json")?.readText()!!, Secret::class.java) }
    }
}

fun secret() = Secret.instance