package com.wefox.spring2.spring2.consumer

import com.wefox.spring2.spring2.domain.MyData
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class MyKafkaListener {
    private val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["spring2-topic"])
    fun consume(myData: MyData) {
        log.info("myData {}", myData)
    }
}