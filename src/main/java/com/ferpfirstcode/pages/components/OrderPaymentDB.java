package com.ferpfirstcode.pages.components;

public class OrderPaymentDB {
    private final double amount;
    private final double payAmount;

    public OrderPaymentDB(double amount, double payAmount) {
        this.amount = amount;
        this.payAmount = payAmount;
    }

    public double getAmount() {
        return amount;
    }

    public double getPayAmount() {
        return payAmount;
    }
}
