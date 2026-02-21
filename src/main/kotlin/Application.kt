package com.github.emberlyte

import com.github.emberlyte.constats.BotConstats
import com.github.emberlyte.dto.Config
import com.github.emberlyte.service.IpService
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.ExperimentalHoplite
import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.api.telegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onContentMessage
import dev.inmo.tgbotapi.types.message.HTML
import dev.inmo.tgbotapi.types.message.content.TextContent
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("BotLogger")

@OptIn(ExperimentalHoplite::class)
suspend fun main() {
    logger.info("Bot is starting...")

    val ipRegex = Regex("""\b(?:\d{1,3}\.){3}\d{1,3}\b""")

    val config = ConfigLoaderBuilder.default().withExplicitSealedTypes().build().loadConfigOrThrow<Config>("/application.yaml")

    val bot = telegramBot(config.botConfig.botToken)

    val ipService = IpService()

    val constant: BotConstats = BotConstats


    bot.buildBehaviourWithLongPolling {
        onCommand("start") {
            reply(it, constant.welcomeMessage, parseMode = HTML)
        }

        onCommand("privacy") {
            reply(it, constant.privacyMessage, parseMode = HTML)
        }

        onContentMessage { message ->
            val content = message.content

            if (content is TextContent) {
                val text = content.text

                val ip = ipRegex.find(text)?.value

                if (text.startsWith("/")) return@onContentMessage

                if (ip != null) {
                    reply(message, "Получил ваш айпи! Oжидайте")

                    val result = ipService.sendRequest(ip)

                    val messageText = """
🔍 <b>Информация об IP:</b> <code>${result?.ip}</code>
🌍 <b>Континент:</b> ${result?.continent}
🗺️ <b>Страна:</b> ${result?.country} (${result?.countryCode})
🏢 <b>Провайдер:</b> ${result?.asName}
🔢 <b>ASN:</b> <code>${result?.asn}</code>
🌐 <b>Домен:</b> ${result?.asDomain}
""".trimIndent()

                    reply(message, messageText, parseMode = HTML)
                } else {
                    reply(message, "Я получил ваш текст, но IP-адреса в нем нет.")
                }

            } else {
                reply(message, "Я пока умею искать IP только в текстовых сообщениях")
            }
        }
    }.join()
}
