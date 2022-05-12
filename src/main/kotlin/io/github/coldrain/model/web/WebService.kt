package io.github.coldrain.model.web

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*

/**
 * io.github.coldrain.model.web.WebService
 * feedback-wall-backend
 *
 * @author 寒雨
 * @since 2022/5/7 21:34
 **/
interface WebService {

    suspend fun getDepartment(studentId: String): String?

    companion object : WebService {
        private val client = HttpClient(CIO)

        override suspend fun getDepartment(studentId: String): String? {
            return client.get("https://be-prod.redrock.cqupt.edu.cn/magipoke-text/search/people?stu=$studentId")
                .body<QueryResult>()
                .data.getOrNull(0)?.major
        }
    }
}

internal data class QueryResult(
    val status: Int,
    val data: List<Data>
) {
    data class Data(
        val stunum: String,
        val name: String,
        val gender: String,
        val classnum: String,
        val major: String,
        val depart: String,
        val grade: String
    )
}