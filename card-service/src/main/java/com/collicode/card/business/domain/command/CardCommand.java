package com.collicode.card.business.domain.command;

import com.collicode.card.business.domain.valueobject.CardType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CardCommand {
    private long cardId;
    private String alias;
    private long accountId;
    private CardType cardType;
    private String pan;
    private String cvv;

}
