package com.collicode.account.business.domain.command;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AccountCommand {
    private String iban;
    private String bicSwift;
    private long customerId;
}
