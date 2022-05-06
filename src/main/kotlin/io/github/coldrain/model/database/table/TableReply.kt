package io.github.coldrain.model.database.table

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

/**
 * io.github.coldrain.model.database.table.TableReply
 * feedback-wall-backend
 *
 * @author 寒雨
 * @since 2022/5/6 13:02
 **/
object TableReply : LongIdTable() {
    val user = reference("user", TableUser)
    val post = reference("post", TablePost)
    val textContent = text("text_content")
    val imageContent = text("image_content").nullable()
    val createAt = datetime("create_at")
    val replyId = long("reply").nullable()
}

class Reply(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Reply>(TableReply)

    var user by User referencedOn TableReply.user
    var post by Post referencedOn TableReply.post
    var textContent by TableReply.textContent
    var imageContent by TableReply.imageContent
    var createAt by TableReply.createAt
    var replyId by TableReply.replyId
}