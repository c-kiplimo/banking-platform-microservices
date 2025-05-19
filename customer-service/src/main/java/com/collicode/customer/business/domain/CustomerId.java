package com.collicode.customer.business.domain;

import com.collicode.shared.domain.Identity;


public class CustomerId extends Identity<CustomerId, Long, Long> {

    private static final long START_POINT = 1_000_000L;
    private static final long END_POINT = 9_999_999_999L;
    private static final long DEFAULT_FACTOR = 5L;

    private CustomerId(Long currentId, Long factor) {
        super(validateStart(currentId), validateFactor(factor));
    }

    public static CustomerId of(Long offset, Long factor) {
        return new CustomerId(offset, factor);
    }

    public static CustomerId of(Long offset) {
        return new CustomerId(offset, DEFAULT_FACTOR);
    }

    public static CustomerId initialId() {
        return new CustomerId(START_POINT, DEFAULT_FACTOR);
    }

    private static Long validateStart(Long id) {
        if (id == null || id < START_POINT) {
            return START_POINT;
        }
        return id;
    }

    private static Long validateFactor(Long factor) {
        if (factor == null || factor < 1) {
            return DEFAULT_FACTOR;
        }
        return factor;
    }

    @Override
    public CustomerId nextId() {
        long next = currentId + factor;
        if (next > END_POINT) {
            throw new IllegalStateException("ID has exceeded the allowed maximum limit.");
        }
        return new CustomerId(next, factor);
    }
}
