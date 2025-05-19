package com.collicode.card.infrastructure.dto.request;

import lombok.Data;

@Data
public class CardRequest {
    private long cardId;
    private String alias;
    private long accountId;
    private String cardType;
    private String pan;
    private String cvv;
}
