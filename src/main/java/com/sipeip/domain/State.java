package com.sipeip.domain;

public enum State {
    ACTIVE("CREADO"),
    IN_REVIEW("EN REVISION"),
    APPROVED("APROBADO"),
    REJECTED("DEVUELTO");

    private final String value;

    State(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
