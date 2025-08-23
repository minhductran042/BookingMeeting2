package com.dtsvn.bookingmeeting.domain.enumeration;

/**
 * The ParticipantResponseStatus enumeration.
 */
public enum ParticipantResponseStatus {
    PENDING("Pending"),
    ACCEPTED("Accepted"),
    DECLINED("Declined"),
    TENTATIVE("Tentative");

    private final String value;

    ParticipantResponseStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

