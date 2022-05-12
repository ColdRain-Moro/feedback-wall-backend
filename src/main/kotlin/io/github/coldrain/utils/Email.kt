package io.github.coldrain.utils

import io.github.coldrain.model.database.DatabaseInit
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.HtmlEmail


/**
 * io.github.coldrain.utils.Email
 * feedback-wall-backend
 *
 * @author 寒雨
 * @since 2022/5/7 0:50
 **/
fun sendVerifyCodeEmail(to: String, code: String) {
    val email = HtmlEmail()
    // 发送邮件的SMTP服务器，如果不设置，默认查询系统变量mail.smtp.host的值，没有则会抛出异常
    // org.apache.commons.mail.EmailException: Cannot find valid hostname for mail session
    email.hostName = "smtp.exmail.qq.com"
    email.setSmtpPort(465)
    email.setCharset("UTF-8")
    // javax.mail.AuthenticationFailedException: 535 Login fail. Authorization code is expired
    email.setAuthenticator(DefaultAuthenticator(secret().email, secret().emailPassword))
    email.isSSLOnConnect = true
    email.setFrom(secret().email)
    email.subject = "验证码"
    email.setHtmlMsg(DatabaseInit.javaClass.classLoader.getResource("email-template.html")?.readText()!!.replace("\${code}", code))
    email.addTo(to)
    email.send()
}