package io.github.coldrain.model.database.table

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

/**
 * io.github.coldrain.model.database.table.TableLike
 * feedback-wall-backend
 *
 * @author 寒雨
 * @since 2022/5/6 13:24
 **/
object TableLike : LongIdTable() {
    val user = reference("user", TableUser)
    // 是否是回复
    val isReply = bool("is_reply")
    val targetId = long("target_id")
}

class Like(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Like>(TableLike)

    var user by User referencedOn TableLike.user
    var isReply by TableLike.isReply
    var targetId by TableLike.targetId
}