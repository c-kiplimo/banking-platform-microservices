package com.collicode.customer.infrastructure.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomerResponse {
    private long customerId;
    private String firstName;
    private String lastName;
    private String otherName;

    public CustomerResponse withCustomerId(Long currentId) {
        this.customerId = currentId;
        return this;
    }
}
