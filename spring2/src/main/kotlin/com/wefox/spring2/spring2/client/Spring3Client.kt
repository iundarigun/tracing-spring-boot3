package com.wefox.spring2.spring2.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "spring3Client", url = "http://localhost:1981/spring3")
interface Spring3Client {
    @GetMapping
    fun get(): String
}