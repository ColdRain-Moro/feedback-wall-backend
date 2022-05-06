package io.github.coldrain.routes

import io.github.coldrain.model.bean.BaseResponse
import io.github.coldrain.model.database.table.TableUser
import io.github.coldrain.model.database.table.User
import io.github.coldrain.utils.makeToken
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.collections.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

/**
 * io.github.coldrain.routes.Authentication
 * feedback-wall-backend
 *
 * @author 寒雨
 * @since 2022/5/5 21:13
 **/
private val phoneVerifyCodeTemp = ConcurrentMap<String, String>()
private val emailVerifyCodeTemp = ConcurrentMap<String, String>()

fun Routing.authentication() {
    route("user") {
        /**
         * route user/login
         * method POST
         * desc 用户登录
         * field account 用户账号
         * field input 用户输入(md5密码或验证码)
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
                        if (phoneVerifyCodeTemp[user.phone] == input) {
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
                        if (user.md5Password != input) {
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
         * field? input 验证码
         * field? image 图片
         * field method 方式(email/card)
         */
        post("/register") {

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

            }
            /**
             * route verify/check
             * method POST
             * desc 验证验证码
             * field input 用户输入(手机号/邮箱)
             * field code 验证码
             * field method 方式(email/phone)
             */
            post("/check") {

            }
        }
    }
}