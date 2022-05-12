package io.github.coldrain.routes

import com.qcloud.cos.model.ObjectMetadata
import com.qcloud.cos.model.PutObjectRequest
import io.github.coldrain.model.bean.BaseResponse
import io.github.coldrain.model.database.table.TableUser
import io.github.coldrain.model.database.table.User
import io.github.coldrain.model.database.table.UserRegisterRequest
import io.github.coldrain.model.web.WebService
import io.github.coldrain.utils.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.collections.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/**
 * io.github.coldrain.routes.Authentication
 * feedback-wall-backend
 *
 * @author 寒雨
 * @since 2022/5/5 21:13
 **/
private val phoneVerifyCodeTemp = ConcurrentMap<String, Pair<Job, String>>()
private val emailVerifyCodeTemp = ConcurrentMap<String, Pair<Job, String>>()

fun Route.authentication() {
    route("user") {
        /**
         * route user/login
         * method POST
         * desc 用户登录
         * field account 用户账号
         * field input 用户输入(密码或验证码)
         * field method 方式(phone/password)
         */
        post("/login") {
            val params = call.receiveParameters()
            val account = params["account"] ?: return@post let {
                call.respond(BaseResponse(-1, "account is null", null))
            }
            val input = params["input"] ?: return@post let {
                call.respond(BaseResponse(-1, "input is null", null))
            }
            val method = params["method"] ?: return@post let {
                call.respond(BaseResponse(-1, "method is null", null))
            }
            when (method) {
                "phone" -> {
                    newSuspendedTransaction {
                        val user = User.find { TableUser.phone eq account }.firstOrNull()
                        if (user == null) {
                            call.respond(BaseResponse(-1, "不存在的账号", null))
                            return@newSuspendedTransaction
                        }
                        if (phoneVerifyCodeTemp[user.phone] != null) {
                            val (job, code) = phoneVerifyCodeTemp[user.phone]!!
                            if (code != input) {
                                call.respond(BaseResponse(-1, "验证码错误", null))
                                return@newSuspendedTransaction
                            }
                            job.cancel()
                            phoneVerifyCodeTemp.remove(user.phone)
                            val accessToken = makeToken(user.id.value, false)
                            val refreshToken = makeToken(user.id.value, true)
                            call.respond(
                                BaseResponse(
                                    0, "登录成功", mapOf(
                                        "access_token" to accessToken,
                                        "refresh_token" to refreshToken
                                    )
                                )
                            )
                        } else {
                            call.respond(BaseResponse(-1, "验证码已过期", null))
                        }
                    }
                }
                "password" -> {
                    newSuspendedTransaction {
                        val user = User.find { TableUser.phone eq account }.firstOrNull()
                        if (user == null) {
                            call.respond(BaseResponse(-1, "不存在的账号", null))
                            return@newSuspendedTransaction
                        }
                        // 加盐之后进行散列
                        if (user.md5Password != (input + user.salt).md5()) {
                            call.respond(BaseResponse(-1, "密码错误", null))
                            return@newSuspendedTransaction
                        }
                        val accessToken = makeToken(user.id.value, false)
                        val refreshToken = makeToken(user.id.value, true)
                        call.respond(
                            BaseResponse(
                                0, "登录成功", mapOf(
                                    "access_token" to accessToken,
                                    "refresh_token" to refreshToken
                                )
                            )
                        )
                    }
                }
                else -> {
                    call.respond(BaseResponse(-1, "wrong method", null))
                    return@post
                }
            }
        }
        /**
         * route user/register
         * method POST
         * desc 用户注册
         * field phone 手机号
         * field password 密码
         * field? email 邮箱
         * field? input 验证码
         * field? image 图片
         * field method 方式(email/card)
         */
        post("/register") {
            val params = call.receiveMultipart()
            val phone = params.value("phone") ?: return@post let {
                call.respond(BaseResponse(-1, "缺少手机号", null))
            }
            val input = params.value("input")
            val image = params.file("image")
            val method = params.value("method") ?: return@post let {
                call.respond(BaseResponse(-1, "缺少方式", null))
            }
            val password = params.value("password") ?: return@post let {
                call.respond(BaseResponse(-1, "缺少密码", null))
            }
            when (method) {
                "email" -> {
                    val email = params.value("email") ?: return@post let {
                        call.respond(BaseResponse(-1, "缺少邮箱", null))
                    }
                    if (!email.isStudentEmail()) {
                        call.respond(BaseResponse(-1, "非校园邮箱", null))
                        return@post
                    }
                    val stuId = email.split("@")[0]
                    if (emailVerifyCodeTemp[email] == null) {
                        call.respond(BaseResponse(-1, "邮箱验证码已过期", null))
                        return@post
                    }
                    if (!newSuspendedTransaction { User.find { TableUser.studentId eq stuId }.empty() }) {
                        call.respond(BaseResponse(-1, "用户已存在", null))
                        return@post
                    }
                    val depart = WebService.getDepartment(stuId) ?: return@post let {
                        call.respond(BaseResponse(-1, "学号不存在", null))
                    }
                    val (job, code) = emailVerifyCodeTemp[email]!!
                    if (code != input) {
                        call.respond(BaseResponse(-1, "邮箱验证码错误", null))
                        return@post
                    }
                    job.cancel()
                    emailVerifyCodeTemp.remove(email)
                    val user = newSuspendedTransaction {
                        User.new {
                            val uuid = UUID.randomUUID().toString()
                            salt = uuid
                            md5Password = (password + salt).md5()
                            this.phone = phone
                            this.email = email
                            this.stuId = stuId
                            department = depart
                        }
                    }
                    val accessToken = makeToken(user.id.value, false)
                    val refreshToken = makeToken(user.id.value, true)
                    call.respond(
                        BaseResponse(0, "注册成功", mapOf(
                            "access_token" to accessToken,
                            "refresh_token" to refreshToken
                            )
                        )
                    )
                }
                "card" -> {
                    val uuid = UUID.randomUUID().toString()
                    image?.streamProvider?.invoke()?.use {
                        val req = PutObjectRequest(BUCKET, "id_card/$uuid.jpg", it, ObjectMetadata())
                        cosClient.putObject(req)
                    } ?: return@post let {
                        call.respond(BaseResponse(-1, "缺少image字段", null))
                    }
                    val url = cosClient.getObjectUrl(BUCKET, "id_card/$uuid.jpg").toString()
                    newSuspendedTransaction {
                        UserRegisterRequest.new {
                            this.phone = phone
                            this.image = url
                        }
                    }
                    call.respond(BaseResponse(0, "请求已提交", null))
                }
                else -> {
                    call.respond(BaseResponse(-1, "wrong method", null))
                    return@post
                }
            }
        }

        route("/verify") {
            /**
             * route verify/send
             * method POST
             * desc 发送验证码
             * field input 用户输入(手机号/邮箱)
             * field method 方式(email/phone)
             */
            post("/send") {
                val params = call.receiveParameters()
                val input = params["input"] ?: return@post let {
                    call.respond(BaseResponse(-1, "input不能为空", null))
                }
                val method = params["method"] ?: return@post let {
                    call.respond(BaseResponse(-1, "method不能为空", null))
                }
                when (method) {
                    "email" -> {
                        if (!input.isEmail()) {
                            call.respond(BaseResponse(-1, "邮箱格式不正确", null))
                            return@post
                        }
                        val code = Random.nextInt(100000, 999999).toString()
                        val job = globalLaunch {
                            delay(TimeUnit.MINUTES.toMillis(10))
                            emailVerifyCodeTemp.remove(input)
                        }
                        emailVerifyCodeTemp[input] = job to code
                        sendVerifyCodeEmail(input, code)
                    }
                    "phone" -> {
                        if (!input.isPhoneNumber()) {
                            call.respond(BaseResponse(-1, "手机号格式不正确", null))
                            return@post
                        }
                        // TODO 接入短信验证码sdk
                    }
                    else -> {
                        call.respond(BaseResponse(-1, "wrong method", null))
                        return@post
                    }
                }
            }
        }
    }
}