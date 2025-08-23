package com.dtsvn.bookingmeeting.domain.enumeration;

/**
 * The BookingStatus enumeration.
 */
public enum BookingStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed");

    private final String value;

    BookingStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

