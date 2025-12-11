package org.ram.product.model;

public class SalaryProduct extends Product{
    private int minAge;
    private double minTransactionAmount;

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public double getMinTransactionAmount() {
        return minTransactionAmount;
    }

    public void setMinTransactionAmount(double minTransactionAmount) {
        this.minTransactionAmount = minTransactionAmount;
    }
}
