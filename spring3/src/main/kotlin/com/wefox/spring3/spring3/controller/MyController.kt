package com.wefox.spring3.spring3.controller

import com.wefox.spring3.spring3.client.Spring2Client
import com.wefox.spring3.spring3.configuration.QueueConfig
import com.wefox.spring3.spring3.domain.MyData
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("spring3")
class MyController(
    private val rabbitTemplate: RabbitTemplate,
    private val kafkaTemplate: KafkaTemplate<String, MyData>,
    private val spring2Client: Spring2Client
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun get(): String {
        logger.info("get Spring3")

        return "Get Spring3"
    }

    @GetMapping("client")
    fun getClient(): String {
        logger.info("get Spring3 to spring3")

        return spring2Client.get()
    }

    @PostMapping("rabbit/internal")
    fun postRabbit() {
        logger.info("post rabbit internal")
        rabbitTemplate.convertAndSend(QueueConfig.exchange, QueueConfig.queue2, MyData("oriol"))
    }

    @PostMapping("rabbit/external")
    fun postRabbitExternal() {
        logger.info("post rabbit external")
        rabbitTemplate.convertAndSend(QueueConfig.exchange, QueueConfig.queue, MyData("oriol"))
    }

    @PostMapping("kafka/internal")
    fun postKafkaInternal() {
        logger.info("post kafka internal")
        kafkaTemplate.send(
            ProducerRecord("spring3-topic",
            UUID.randomUUID().toString(),
            MyData("coming from spring 3")).also {
                it.headers().add("MY-HEADER", "TEST".toByteArray())
            }
        )
    }

    @PostMapping("kafka/external")
    fun postKafkaExternal() {
        logger.info("post external")
        kafkaTemplate.send(
            "spring2-topic",
            UUID.randomUUID().toString(),
            MyData("coming from spring 3")
        )
    }
}