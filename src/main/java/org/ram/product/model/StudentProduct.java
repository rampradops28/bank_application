package org.ram.product.model;

public class StudentProduct extends Product{

    private int maxAge;
    private double maxTransactionAmount;

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public double getMaxTransactionAmount() {
        return maxTransactionAmount;
    }

    public void setMaxTransactionAmount(double maxTransactionAmount) {
        this.maxTransactionAmount = maxTransactionAmount;
    }
}
