package io.github.coldrain.model.database.table

import io.github.coldrain.utils.formJson
import io.github.coldrain.utils.toJson
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

/**
 * io.github.coldrain.model.database.table.TablePost
 * feedback-wall-backend
 *
 * @author 寒雨
 * @since 2022/5/6 12:47
 **/
object TablePost : LongIdTable() {
    val title = varchar("title", 255)
    val textContent = text("text_content")
    val imageContent = text("image_content").nullable()
    val createAt = datetime("create_at")
    val updateAt = datetime("update_at")
    val lastReplyAt = datetime("last_reply_At")
    val replyCount = integer("reply_count")
    val viewCount = integer("view_count")
    val author = reference("author", TableUser)
    val tags = text("tags").nullable()
}

class Post(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Post>(TablePost)

    var title by TablePost.title
    var textContent by TablePost.textContent
    var imageContent by TablePost.imageContent
    var createAt by TablePost.createAt
    var updateTime by TablePost.updateAt
    var lastReplyAt by TablePost.lastReplyAt
    var replyCount by TablePost.replyCount
    var viewCount by TablePost.viewCount
    var author by User referencedOn TablePost.author
    var tags by TablePost.tags.transform({ it?.toJson() }, { it?.formJson<List<String>>() })

}