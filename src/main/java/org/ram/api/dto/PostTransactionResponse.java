package org.ram.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PostTransactionResponse implements Serializable {

    public String postingNumber;
    public String accountNumber;
    public String paymentType;
    public BigDecimal amount;


    public PostTransactionResponse() { }

    public PostTransactionResponse(String postingNumber, String accountNumber, String paymentType, BigDecimal amount) {
        this.postingNumber = postingNumber;
        this.accountNumber = accountNumber;
        this.paymentType = paymentType;
        this.amount = amount;
    }

    public String getPostingNumber() {
        return postingNumber;
    }

    public void setPostingNumber(String postingNumber) {
        this.postingNumber = postingNumber;
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
}