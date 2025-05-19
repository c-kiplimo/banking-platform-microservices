package com.collicode.card.infrastructure.dto.response;

import lombok.Data;

@Data
public class AccountResponse {
    private long recordId;
    private String iban;
    private String bicSwift;
    private long customerId;
}