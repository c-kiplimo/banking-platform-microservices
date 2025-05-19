package com.collicode.account.infrastructure.repository.model.read;


import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("account")
@Data
public class AccountReadModel {
    private long recordId;
    private String iban;
    private String bicSwift;
    private long customerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
