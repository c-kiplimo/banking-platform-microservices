package com.collicode.customer.infrastructure.repository.model.write;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("customer")
@Data
@Builder
public class CustomerWriteModel {
    private long recordId;
    private String firstName;
    private String lastName;
    private String otherName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
