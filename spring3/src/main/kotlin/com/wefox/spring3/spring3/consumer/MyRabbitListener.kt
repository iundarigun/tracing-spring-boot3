package com.wefox.spring3.spring3.consumer

import com.wefox.spring3.spring3.domain.MyData
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class MyRabbitListener {
    private val logger = LoggerFactory.getLogger(javaClass)

    @RabbitListener(queues = ["SECOND-QUEUE"])
    fun listener(myData: MyData) {
        logger.info("mydata $myData")
    }
}