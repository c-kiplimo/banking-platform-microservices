package com.collicode.card.business.domain;

import com.collicode.card.business.domain.command.CardCommand;
import com.collicode.card.business.domain.valueobject.CardType;
import com.collicode.shared.domain.BusinessEntity;
import lombok.Getter;

import java.util.Objects;

import static com.collicode.card.business.domain.constants.CardConstants.ENTITYNAME;
import static com.collicode.shared.exception.BusinessEntityException.invalidEntryException;
import static com.collicode.shared.exception.BusinessEntityException.missingKeyException;
import static com.collicode.shared.util.BusinessValidation.ifHasNoText;
import static com.collicode.shared.util.BusinessValidation.ifTrue;

@Getter
public class Card extends BusinessEntity {

    private final CardId cardId;
    private final String alias;
    private final long accountId;
    private final CardType cardType;
    private final String pan;
    private final String cvv;

    private Card(CardBuilder builder) {
        this.cardId = builder.cardId;
        this.alias = builder.alias;
        this.accountId = builder.accountId;
        this.cardType = builder.cardType;
        this.pan = builder.pan;
        this.cvv = builder.cvv;
    }

    public static Card from(CardCommand command, CardId cardId) {
        return builder()
                .cardId(cardId)
                .alias(command.getAlias())
                .accountId(command.getAccountId())
                .cardType(command.getCardType())
                .pan(command.getPan())
                .cvv(command.getCvv())
                .build();
    }

    public static CardBuilder builder() {
        return new CardBuilder();
    }

    @Override
    public void validate() {
        ifTrue(Objects.isNull(this.cardId), missingKeyException(ENTITYNAME, "CardId"));
        ifTrue(this.accountId <= 0, missingKeyException(ENTITYNAME, "AccountId"));
        ifTrue(this.cardType == null, missingKeyException(ENTITYNAME, "CardType"));
        ifHasNoText(this.pan, missingKeyException(ENTITYNAME, "PAN"));
        ifHasNoText(this.cvv, missingKeyException(ENTITYNAME, "CVV"));
        ifTrue(this.cvv.length() != 3, invalidEntryException(ENTITYNAME, "CVV"));
    }

    public static class CardBuilder {
        private CardId cardId;
        private String alias;
        private long accountId;
        private CardType cardType;
        private String pan;
        private String cvv;

        public CardBuilder cardId(CardId cardId) {
            this.cardId = cardId;
            return this;
        }

        public CardBuilder alias(String alias) {
            this.alias = alias;
            return this;
        }

        public CardBuilder accountId(long accountId) {
            this.accountId = accountId;
            return this;
        }

        public CardBuilder cardType(CardType cardType) {
            this.cardType = cardType;
            return this;
        }

        public CardBuilder pan(String pan) {
            this.pan = pan;
            return this;
        }

        public CardBuilder cvv(String cvv) {
            this.cvv = cvv;
            return this;
        }

        public Card build() {
            return new Card(this);
        }
    }
}
