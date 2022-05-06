package io.github.coldrain.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * io.github.coldrain.utils.Coroutine
 * feedback-wall-backend
 *
 * @author 寒雨
 * @since 2022/5/6 17:24
 **/
private val globalScope = CoroutineScope(Dispatchers.Unconfined)

fun globalLaunch(block: suspend () -> Unit) = globalScope.launch { block() }