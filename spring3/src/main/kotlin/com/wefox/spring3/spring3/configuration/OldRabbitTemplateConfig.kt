package com.wefox.spring3.spring3.configuration

import brave.Tracing
import brave.propagation.B3Propagation
import brave.propagation.B3SinglePropagation
import brave.propagation.Propagation.Getter
import brave.propagation.TraceContext
import brave.spring.rabbit.SpringRabbitTracing
import jakarta.annotation.PostConstruct
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessagePostProcessor
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.math.BigInteger

/**
 * First approach using post processors
 */
class OldRabbitTemplateConfig(
    private val rabbitTemplate: RabbitTemplate,
    private val tracing: Tracing,
    private val listener: SimpleRabbitListenerContainerFactory,
) {
    @PostConstruct
    fun add() {
        rabbitTemplate.addBeforePublishPostProcessors(object : MessagePostProcessor {
            override fun postProcessMessage(message: Message): Message {
                message.messageProperties.setHeader("TEST", "test")
                message.messageProperties.setHeader("traceparent",
                    "00-${tracing.currentTraceContext().get().traceIdString()}-${tracing.currentTraceContext().get().spanIdString()}-00")
                message.messageProperties.setHeader("b3",
                    "${tracing.currentTraceContext().get().traceIdString()}-${tracing.currentTraceContext().get().spanIdString()}-00")
                return message
            }
        })

        listener.setAfterReceivePostProcessors(object : MessagePostProcessor {
            override fun postProcessMessage(message: Message): Message {
                message.messageProperties.getHeader<String>("traceparent")?.let {

                    tracing.tracer().startScopedSpanWithParent(Thread.currentThread().name,
                        traceContextByParentTrace(it))
                } ?: message.messageProperties.getHeader<String>("b3")?.let {

                    tracing.tracer().startScopedSpanWithParent(Thread.currentThread().name,
                        traceContextByB3(it))
                }
                return message
            }
        })
    }

    private fun traceContextByParentTrace(traceParent: String): TraceContext {
        val numberTraceId = BigInteger(traceParent.split("-")[1], 16).toLong()
        val numberSpanId = BigInteger(traceParent.split("-")[2], 16).toLong()
        return TraceContext.newBuilder()
            .traceId(numberTraceId)
            .spanId(numberSpanId)
            .build()
    }

    private fun traceContextByB3(b3: String): TraceContext {
        val numberTraceId = BigInteger(b3.split("-")[0], 16).toLong()
        val numberSpanId = BigInteger(b3.split("-")[1], 16).toLong()
        return TraceContext.newBuilder()
            .traceId(numberTraceId)
            .spanId(numberSpanId)
            .build();
    }
}