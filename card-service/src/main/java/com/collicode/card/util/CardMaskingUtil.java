package com.collicode.card.util;


import com.collicode.card.business.domain.Card;
import com.collicode.card.business.domain.valueobject.CardType;

public class CardMaskingUtil {

    public static String maskPan(String pan) {
        if (pan == null || pan.length() < 8) return "****";
        return pan.substring(0, 4) + "****" + pan.substring(pan.length() - 4);
    }

    public static String maskCvv(String cvv) {
        return "***";
    }

    public static MaskedCardDto toMaskedDto(Card card, boolean showSensitive) {
        return new MaskedCardDto(
                card.getCardId().getCurrentId(),
                card.getAlias(),
                card.getAccountId(),
                card.getCardType(),
                showSensitive ? card.getPan() : maskPan(card.getPan()),
                showSensitive ? card.getCvv() : maskCvv(card.getCvv())
        );
    }

    public record MaskedCardDto(
            long cardId,
            String alias,
            long accountId,
            CardType cardType,
            String pan,
            String cvv
    ) {
    }
}

