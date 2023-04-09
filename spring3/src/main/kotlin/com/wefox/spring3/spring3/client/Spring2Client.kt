package com.wefox.spring3.spring3.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "spring2Client", url = "http://localhost:1980/spring2")
interface Spring2Client {
    @GetMapping
    fun get(): String
}