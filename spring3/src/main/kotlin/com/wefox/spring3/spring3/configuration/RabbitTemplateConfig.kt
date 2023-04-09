package com.wefox.spring3.spring3.configuration

import brave.Tracing
import brave.propagation.B3Propagation
import brave.propagation.B3SinglePropagation
import brave.propagation.Propagation.Getter
import brave.propagation.TraceContext
import brave.spring.rabbit.SpringRabbitTracing
import jakarta.annotation.PostConstruct
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessagePostProcessor
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.math.BigInteger

@Configuration
class RabbitTemplateConfig(
    private val tracing: Tracing,
    private val connectionFactory: ConnectionFactory
) {

    @Bean
    fun rabbitListenerContainerFactory(messageConverter: MessageConverter): SimpleRabbitListenerContainerFactory {
        return SpringRabbitTracing.create(tracing).newSimpleRabbitListenerContainerFactory(connectionFactory).also {
            it.setMessageConverter(messageConverter)
        }
    }

    @Bean
    fun rabbitTemplate(messageConverter: MessageConverter): RabbitTemplate {
        return SpringRabbitTracing.create(tracing).newRabbitTemplate(connectionFactory).also {
            it.messageConverter = messageConverter
        }
    }
}