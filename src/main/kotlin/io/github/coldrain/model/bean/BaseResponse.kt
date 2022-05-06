package io.github.coldrain.model.bean

/**
 * io.github.coldrain.model.bean.BaseResponse
 * feedback-wall-backend
 *
 * @author 寒雨
 * @since 2022/5/6 11:02
 **/
data class BaseResponse<T>(
    val code: Int,
    val msg: String,
    val data: T?
)
