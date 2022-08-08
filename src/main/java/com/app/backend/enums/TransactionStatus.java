package com.app.backend.enums;

public enum TransactionStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    CANCELED("Canceled"),
    FAILED("Failed");

    public final String label;

    TransactionStatus(String label){

        this.label = label;
    }
}
