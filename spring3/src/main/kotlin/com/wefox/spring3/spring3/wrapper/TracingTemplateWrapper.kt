package com.wefox.spring3.spring3.wrapper

import brave.Tracing
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.amqp.core.Message
import org.springframework.stereotype.Component
import java.awt.Rectangle


//@Aspect
//@Component
class TracingTemplateWrapper(private val tracing: Tracing) {
    @Before("execution(* org.springframework.amqp.rabbit.core.RabbitTemplate.send(..))")
    fun addHeader(jointPoint: JoinPoint) {
        jointPoint.args.asList().find { it is Message }?.let {
            (it as Message).messageProperties.setHeader("test", "test1")
        }
    }

}