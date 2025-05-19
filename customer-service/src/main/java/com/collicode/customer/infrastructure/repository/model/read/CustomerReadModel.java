package com.collicode.customer.infrastructure.repository.model.read;


import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("customer")
@Data
public class CustomerReadModel {
    private long recordId;
    private String firstName;
    private String lastName;
    private String otherName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
