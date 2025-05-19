package com.collicode.account.infrastructure.dto.response;

import lombok.Data;

@Data
public class CustomerResponse {
    private long recordId;
    private String firstName;
    private String lastName;
    private String otherName;
}
