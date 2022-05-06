package io.github.coldrain.model.database

import io.github.coldrain.model.database.table.TablePost
import io.github.coldrain.model.database.table.TableReply
import io.github.coldrain.model.database.table.TableUser
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * io.github.coldrain.model.database.DatabaseInit
 * feedback-wall-backend
 *
 * @author 寒雨
 * @since 2022/5/6 11:16
 **/
object DatabaseInit {
    fun init() {
        Database.connect(
            "jdbc:mysql://localhost:3306/feedback_wall",
            "com.mysql.cj.jdbc.Driver",
            "root",
            "!Ghy030608"
        )
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(
                TableReply,
                TablePost,
                TableUser
            )
        }
    }
}