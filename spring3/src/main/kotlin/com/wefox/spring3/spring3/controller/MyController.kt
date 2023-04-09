package com.wefox.spring3.spring3.controller

import com.wefox.spring3.spring3.configuration.QueueConfig
import com.wefox.spring3.spring3.domain.MyData
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("spring3")
class MyController(private val rabbitTemplate: RabbitTemplate) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun get(): String {
        logger.info("get Spring3")

        return "Get Spring3"
    }

    @PostMapping("internal")
    fun post() {
        logger.info("post internal")
        rabbitTemplate.convertAndSend(QueueConfig.exchange, QueueConfig.queue2, MyData("oriol"))
    }

    @PostMapping("external")
    fun postExternal() {
        logger.info("post external")
        rabbitTemplate.convertAndSend(QueueConfig.exchange, QueueConfig.queue, MyData("oriol"))
    }
}