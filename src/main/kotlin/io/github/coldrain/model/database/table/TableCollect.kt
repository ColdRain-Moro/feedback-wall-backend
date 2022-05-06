package io.github.coldrain.model.database.table

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

/**
 * io.github.coldrain.model.database.table.TableCollect
 * feedback-wall-backend
 *
 * @author 寒雨
 * @since 2022/5/6 13:40
 **/
object TableCollect : LongIdTable() {
    val user = reference("user", TableUser)
    val post = reference("post", TablePost)
}

class Collect(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Collect>(TableCollect)

    var user by User referencedOn TableCollect.user
    var post by Post referencedOn TableCollect.post
}
