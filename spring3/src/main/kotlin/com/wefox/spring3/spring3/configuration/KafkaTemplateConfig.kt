package com.wefox.spring3.spring3.configuration

import brave.Tracing
import brave.kafka.clients.KafkaTracing
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.ProducerFactory

@Configuration
class KafkaTemplateConfig(
    private val tracing: Tracing,
    private val producerFactory: ProducerFactory<Any, Any>
) {

    @Bean
    fun kafkaTracing(): KafkaTracing = KafkaTracing.create(tracing)

    @PostConstruct
    fun after() {
        producerFactory.addPostProcessor { kafkaTracing().producer(it) }
    }
}