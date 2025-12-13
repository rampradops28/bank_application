package org.ram.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PostTransactionRequest implements Serializable {

    public String accountNumber;
    public String paymentType;
    public BigDecimal amount;
    public LocalDate date;

    public PostTransactionRequest() { }

    public PostTransactionRequest(String accountNumber, String paymentType, BigDecimal amount, LocalDate date) {
        this.accountNumber =  accountNumber;
        this.paymentType = paymentType;
        this.amount = amount;
        this.date = date;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}