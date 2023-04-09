package com.wefox.spring3.spring3.consumer

import brave.Tracer
import brave.kafka.clients.KafkaTracing
import com.wefox.spring3.spring3.domain.MyData
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component


@Component
class MyKafkaListener {
    private val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["spring3-topic"])
    fun consume(myData: ConsumerRecord<String, MyData>) {
        log.info("myData {}", myData.value())
    }
}