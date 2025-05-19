package com.collicode.account.infrastructure.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountResponse {
    private long accountId;
    private String iban;
    private String bicSwift;
    private long customerId;

    public AccountResponse withAccountId(Long currentId) {
        this.accountId = currentId;
        return this;
    }
}
