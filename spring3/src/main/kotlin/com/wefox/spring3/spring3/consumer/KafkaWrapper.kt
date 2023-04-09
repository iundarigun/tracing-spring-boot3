package com.wefox.spring3.spring3.consumer

import brave.Tracer
import brave.kafka.clients.KafkaTracing
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component

@Aspect
@Component
class KafkaWrapper(
    private val kafkaTracing: KafkaTracing,
    private val tracer: Tracer
) {
    @Before("execution(* com.wefox.spring3.spring3.consumer.MyKafkaListener.consume(..))")
    fun startTracing(joinPoint: JoinPoint) {
        joinPoint.args.asList().firstOrNull{it is ConsumerRecord<*,*>}?.let {
        val span = this.kafkaTracing.nextSpan(it as ConsumerRecord<*, *>).name("on-message").start()
        this.tracer.withSpanInScope(span)
        }
    }
}