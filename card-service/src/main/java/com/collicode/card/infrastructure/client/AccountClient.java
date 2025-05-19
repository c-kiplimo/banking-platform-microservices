package com.collicode.card.infrastructure.client;


import com.collicode.card.infrastructure.dto.response.AccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

@FeignClient(name = "account-service", url = "${account.service.url}")
public interface AccountClient {


    @GetMapping("/api/v1/accounts/{accountId}")
    Mono<AccountResponse> getAccountById(@PathVariable("accountId") long accountId);


}
