package com.collicode.card.business.domain.command;

import com.collicode.card.business.domain.valueobject.CardType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CardCommand {
    private final String alias;
    private final long accountId;
    private final CardType cardType;
    private final String pan;
    private final String cvv;
}
