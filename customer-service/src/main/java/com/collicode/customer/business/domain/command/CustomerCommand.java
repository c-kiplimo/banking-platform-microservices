package com.collicode.customer.business.domain.command;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CustomerCommand {
    private String firstName;
    private String lastName;
    private String otherName;
}
