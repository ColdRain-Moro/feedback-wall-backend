package io.github.coldrain.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

/**
 * io.github.coldrain.utils.Utils
 * feedback-wall-backend
 *
 * @author 寒雨
 * @since 2022/5/6 17:05
 **/
val gson: Gson = GsonBuilder().create()

inline fun <reified T> String.formJson(): T {
    return gson.fromJson(this, object : TypeToken<T>() {}.type)
}

fun Any.toJson(): String {
    return gson.toJson(this)
}