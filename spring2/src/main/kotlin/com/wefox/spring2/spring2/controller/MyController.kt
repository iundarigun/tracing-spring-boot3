package com.wefox.spring2.spring2.controller

import com.wefox.spring2.spring2.client.Spring3Client
import com.wefox.spring2.spring2.configuration.QueueConfig.Companion.exchange
import com.wefox.spring2.spring2.configuration.QueueConfig.Companion.queue
import com.wefox.spring2.spring2.configuration.QueueConfig.Companion.queue2
import com.wefox.spring2.spring2.domain.MyData
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("spring2")
class MyController(
    private val spring3Client: Spring3Client,
    private val rabbitTemplate: RabbitTemplate,
    private val kafkaTemplate: KafkaTemplate<String, MyData>
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun get(): String {
        logger.info("get Spring 2")
        return "get Spring 2"
    }

    @GetMapping("client")
    fun getClient() {
        logger.info("client spring2 to spring3")
        val result = spring3Client.get()
        logger.info("result $result")
    }

    @PostMapping("rabbit/internal")
    fun post() {
        logger.info("post internal")
        rabbitTemplate.convertAndSend(exchange, queue, MyData("oriol"))
    }

    @PostMapping("rabbit/external")
    fun postExternal() {
        logger.info("post external")
        rabbitTemplate.convertAndSend(exchange, queue2, MyData("oriol"))
    }

    @PostMapping("kafka/internal")
    fun postKafkaInternal() {
        logger.info("post kafka internal")
        kafkaTemplate.send(
            "spring2-topic",
            UUID.randomUUID().toString(),
            MyData("coming from spring 2")
        )
    }

    @PostMapping("kafka/external")
    fun postKafkaExternal() {
        logger.info("post external")
        kafkaTemplate.send(
            "spring3-topic",
            UUID.randomUUID().toString(),
            MyData("coming from spring 2")
        )
    }
}