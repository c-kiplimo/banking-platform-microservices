package com.collicode.customer.infrastructure.dto.request;

import lombok.Data;

@Data
public class CustomerRequest {
    private long customerId;
    private String firstName;
    private String lastName;
    private String otherName;
}
