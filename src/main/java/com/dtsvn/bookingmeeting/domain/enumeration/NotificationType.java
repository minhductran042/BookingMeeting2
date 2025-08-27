package com.dtsvn.bookingmeeting.domain.enumeration;

/**
 * The NotificationType enumeration.
 */
public enum NotificationType {
    BOOKING_APPROVED("Booking Approved"),
    BOOKING_REJECTED("Booking Rejected"),
    BOOKING_REMINDER("Booking Reminder"),
    GENERAL("General");

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

