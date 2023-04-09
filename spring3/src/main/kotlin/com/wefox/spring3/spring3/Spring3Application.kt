package com.wefox.spring3.spring3

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class Spring3Application

fun main(args: Array<String>) {
	runApplication<Spring3Application>(*args)
}
