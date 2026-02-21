package com.github.emberlyte.service

import com.github.emberlyte.dto.Config
import com.github.emberlyte.dto.IpClass
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.ExperimentalHoplite
import com.sksamuel.hoplite.addEnvironmentSource
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

@OptIn(ExperimentalHoplite::class)
class IpService {

    private val config = ConfigLoaderBuilder.default().addEnvironmentSource().withExplicitSealedTypes().build().loadConfigOrThrow<Config>("/application.yaml")

    private val apiToken = config.apiConfig.apiToken

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    private val logger = LoggerFactory.getLogger("BotLogger")


    suspend fun sendRequest(ip: String): IpClass? {
        return try {
            val response: HttpResponse = httpClient.get("https://api.ipinfo.io/lite/$ip") {
                header("Authorization", "Bearer $apiToken")
            }

            if (response.status.isSuccess()) {
                response.body<IpClass>()
            } else null

        } catch (e: Exception) {
            logger.error("error: $e")
            null
        }
    }
}
