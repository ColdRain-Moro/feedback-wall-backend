package io.github.coldrain.model.database.table

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * io.github.coldrain.model.database.table.TableUserRegisterRequest
 * feedback-wall-backend
 * 注册请求表，人工审核以通过
 *
 * @author 寒雨
 * @since 2022/5/7 0:38
 **/
object TableUserRegisterRequest : IntIdTable() {
    val phone = varchar("phone", 20)
    val image = varchar("image", 255)
    val pass = bool("pass").default(false)
    val reason = varchar("reason", 255).nullable()
}

class UserRegisterRequest(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserRegisterRequest>(TableUserRegisterRequest)

    var phone by TableUserRegisterRequest.phone
    var image by TableUserRegisterRequest.image
    var pass by TableUserRegisterRequest.pass
    var reason by TableUserRegisterRequest.reason
}