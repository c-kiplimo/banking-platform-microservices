package com.collicode.account.infrastructure.dto.request;

import lombok.Data;

@Data
public class AccountRequest {
    private long accountId;
    private String iban;
    private String bicSwift;
    private long customerId;
}
