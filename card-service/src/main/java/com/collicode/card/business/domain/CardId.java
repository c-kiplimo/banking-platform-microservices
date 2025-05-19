package com.collicode.card.business.domain;

import com.collicode.shared.domain.Identity;

public class CardId extends Identity<CardId, Long, Long> {

    private static final long START_POINT = 5_000_000L;
    private static final long END_POINT = 9_999_999_999L;
    private static final long DEFAULT_FACTOR = 10L;

    private CardId(Long currentId, Long factor) {
        super(validateStart(currentId), validateFactor(factor));
    }

    public static CardId of(Long offset, Long factor) {
        return new CardId(offset, factor);
    }

    public static CardId of(Long offset) {
        return new CardId(offset, DEFAULT_FACTOR);
    }

    public static CardId initialId() {
        return new CardId(START_POINT, DEFAULT_FACTOR);
    }

    private static Long validateStart(Long id) {
        return (id == null || id < START_POINT) ? START_POINT : id;
    }

    private static Long validateFactor(Long factor) {
        return (factor == null || factor < 1) ? DEFAULT_FACTOR : factor;
    }

    @Override
    public CardId nextId() {
        long next = currentId + factor;
        if (next > END_POINT) {
            throw new IllegalStateException("Card ID has exceeded the allowed maximum limit.");
        }
        return new CardId(next, factor);
    }
}
