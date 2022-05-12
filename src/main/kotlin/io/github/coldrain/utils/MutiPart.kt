package io.github.coldrain.utils

import io.ktor.http.content.*

/**
 * io.github.coldrain.utils.MutiPart
 * feedback-wall-backend
 *
 * @author 寒雨
 * @since 2022/5/7 20:57
 **/
suspend fun MultiPartData.value(name: String) =
    try { (readAllParts().filter { it.name == name }[0] as? PartData.FormItem)?.value } catch(e: Exception) { null }

suspend fun MultiPartData.file(name: String) =
    try { readAllParts().filter { it.name == name }[0] as? PartData.FileItem } catch(e: Exception) { null }