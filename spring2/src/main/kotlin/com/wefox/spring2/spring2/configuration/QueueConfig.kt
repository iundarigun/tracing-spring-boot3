package com.wefox.spring2.spring2.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.core.ExchangeBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
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
    fun firstQueue(): Queue {
        return QueueBuilder
            .durable(queue)
            .build()
    }
    @Bean
    fun secondQueue(): Queue {
        return QueueBuilder
            .durable(queue2)
            .build()
    }

    @Bean
    fun directExchange(): Exchange {

        return ExchangeBuilder
            .directExchange(exchange)
            .durable(true)
            .build()
    }

    @Bean
    fun firstDirectBinding(): Binding {
        return BindingBuilder
            .bind(firstQueue())
            .to(directExchange())
            .with(queue)
            .noargs()
    }

    @Bean
    fun secondDirectBinding(): Binding {
        return BindingBuilder
            .bind(secondQueue())
            .to(directExchange())
            .with(queue2)
            .noargs()
    }
    @Bean
    fun messageConverter(objectMapper: ObjectMapper): MessageConverter {
        return Jackson2JsonMessageConverter(objectMapper)
    }
}