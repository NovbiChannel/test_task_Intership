package com.example.testtaskintership.data.api

import com.example.testtaskintership.domain.model.MainDataModel
import com.example.testtaskintership.domain.model.SecondaryDataModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

interface ApiService {
    suspend fun getCameras(): MainDataModel
    suspend fun getDoors(): SecondaryDataModel

    companion object {
        fun create(): ApiService {
            return ApiServiceImpl (
                client = HttpClient(Android) {
                    install(Logging) {
                        logger = Logger.Companion.DEFAULT
                        level = LogLevel.ALL
                    }
                    install(ContentNegotiation) {
                        json(Json {
                            prettyPrint = true
                            isLenient = true
                        }
                        )
                    }
                }
            )
        }
    }
}

object ApiRoutes {
    private const val BASE_URL = "https://cars.cprogroup.ru"
    const val CAMERAS = "$BASE_URL/api/rubetek/cameras/"
    const val DOORS = "$BASE_URL/api/rubetek/doors/"
}