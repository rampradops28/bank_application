package org.ram.api.dto;

import java.math.BigDecimal;

public class BalanceResponse {

    private String balanceType;
    private BigDecimal amount;

    public BalanceResponse(String balanceType, BigDecimal amount) {
        this.balanceType = balanceType;
        this.amount = amount;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
