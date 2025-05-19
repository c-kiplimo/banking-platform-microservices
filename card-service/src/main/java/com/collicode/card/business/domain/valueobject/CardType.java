package com.collicode.card.business.domain.valueobject;

import com.collicode.shared.domain.ValueObject;

public enum CardType implements ValueObject {
    VIRTUAL,
    PHYSICAL;

    @Override
    public void validate() {

    }
}
