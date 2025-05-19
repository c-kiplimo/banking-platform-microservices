package com.collicode.account.infrastructure.repository.model.write;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("account")
@Data
@Builder
public class AccountWriteModel {
    private long recordId;
    private String iban;
    private String bicSwift;
    private long customerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
