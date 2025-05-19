package com.collicode.account.infrastructure.client;

import com.collicode.account.infrastructure.dto.response.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

@FeignClient(name = "customer-service", url = "${customer.service.url}")
public interface CustomerClient {


    @GetMapping("/api/v1/customers/{customerId}")
    Mono<CustomerResponse> getcustomerById(@PathVariable("customerId") long customerId);


}
