package com.collicode.card.infrastructure.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CardResponse {
    private long cardId;
    private String alias;
    private long accountId;
    private String cardType;
    private String pan;
    private String cvv;

    public CardResponse withCardId(Long currentId) {
        this.cardId = currentId;
        return this;
    }
}
