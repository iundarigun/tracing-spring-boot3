package com.wefox.spring3.spring3.configuration

import brave.Tracing
import brave.messaging.MessagingTracing
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessagePostProcessor
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QueueConfig {
    companion object {
        const val queue = "FIRST-QUEUE"
        const val queue2 = "SECOND-QUEUE"
        const val exchange = "DIRECT-EXCHANGE"
    }

    @Bean
    fun messageConverter(tracing: Tracing, objectMapper: ObjectMapper): MessageConverter {
        return object: Jackson2JsonMessageConverter(objectMapper) {

            override fun createMessage(
                objectToConvert: Any,
                messageProperties: MessageProperties
            ): Message {
                messageProperties.setHeader("test","uri")
                return super.createMessage(objectToConvert, messageProperties)
            }
        }
    }
}