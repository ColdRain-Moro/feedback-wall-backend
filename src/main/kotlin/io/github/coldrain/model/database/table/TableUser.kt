package io.github.coldrain.model.database.table

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * io.github.coldrain.model.database.table.TableUser
 * feedback-wall-backend
 *
 * @author 寒雨
 * @since 2022/5/6 11:32
 **/
object TableUser : IntIdTable() {
    val md5Password = varchar("md5_password", 50)
    val salt = varchar("salt", 50)
    val phone = varchar("phone", 50)
    val email = varchar("email", 50).nullable()
    val studentId = varchar("student_id", 25)
    val department = varchar("department", 50)
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(TableUser)

    var md5Password by TableUser.md5Password
    var salt by TableUser.salt
    var phone by TableUser.phone
    var email by TableUser.email
    var stuId by TableUser.studentId
    var department by TableUser.department
}