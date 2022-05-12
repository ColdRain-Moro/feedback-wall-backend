package io.github.coldrain.utils

/**
 * io.github.coldrain.utils.Judgement
 * feedback-wall-backend
 *
 * @author 寒雨
 * @since 2022/5/7 20:06
 **/
private val phoneRegex = Regex("^1[3456789]\\d{9}$")
private val emailRegex = Regex("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")
private val studentEmailRegex = Regex("\\d{10}@(stu.cqupt.edu.cn)$")

fun String.isPhoneNumber(): Boolean {
    // 防止恶意注入
    if (length != 11) {
        return false
    }
    return matches(phoneRegex)
}

fun String.isEmail(): Boolean {
    // 防止恶意注入
    if (length > 64) {
        return false
    }
    return matches(emailRegex)
}

fun String.isStudentEmail(): Boolean {
    // 防止恶意注入
    if (length > 64) {
        return false
    }
    return matches(studentEmailRegex)
}