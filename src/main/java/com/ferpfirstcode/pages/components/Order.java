package com.ferpfirstcode.pages.components;

import java.time.LocalDateTime;

public class Order {

    private final long orderNumber;
    private final double total;
    private final LocalDateTime creationTime;

    public Order(long orderNumber, double total, LocalDateTime creationTime) {
        this.orderNumber = orderNumber;
        this.total = total;
        this.creationTime = creationTime;
    }

    public long getOrderNumber() {
        return orderNumber;
    }

    public double getTotal() {
        return total;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }
}