package org.ram.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccountResponse implements Serializable {

    public String accountNumber;
    public String productCode;
    public String productType;
    public String name;
    public int age;
    public BigDecimal balance;

    public AccountResponse() { }

    public AccountResponse(String accountNumber, String productCode, String productType, String name, int age, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.productCode = productCode;
        this.productType = productType;
        this.name = name;
        this.age = age;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}