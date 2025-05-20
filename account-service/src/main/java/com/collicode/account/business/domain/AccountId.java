package com.collicode.account.business.domain;

import com.collicode.shared.domain.Identity;


public class AccountId extends Identity<AccountId, Long, Long> {

    private static final long START_POINT = 1_000_000L;
    private static final long END_POINT = 9_999_999_999L;
    private static final long DEFAULT_FACTOR = 5L;

    AccountId(Long currentId, Long factor) {
        super(validateStart(currentId), validateFactor(factor));
    }

    public static AccountId of(Long offset, Long factor) {
        return new AccountId(offset, factor);
    }

    public static AccountId of(Long offset) {
        return new AccountId(offset, DEFAULT_FACTOR);
    }

    public static AccountId initialId() {
        return new AccountId(START_POINT, DEFAULT_FACTOR);
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
    public AccountId nextId() {
        long next = currentId + factor;
        if (next > END_POINT) {
            throw new IllegalStateException("ID has exceeded the allowed maximum limit.");
        }
        return new AccountId(next, factor);
    }
}
