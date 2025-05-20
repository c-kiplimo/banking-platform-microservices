package com.collicode.account.infrastructure.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountRequest {
    private long accountId;
    private String iban;
    private String bicSwift;
    private long customerId;
}
