package com.collicode.card.infrastructure.repository.model.read;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("card")
@Data
@Builder
public class CardReadModel {
    private final String alias;
    private final long accountId;
    private final String cardType;
    private final String pan;
    private final String cvv;
    private long recordId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
