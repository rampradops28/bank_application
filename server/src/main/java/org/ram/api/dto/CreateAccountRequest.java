package org.ram.api.dto;

import java.io.Serializable;

public class CreateAccountRequest implements Serializable {

    public String productCode;
    public String productType;
    public String name;
    public int age;
    public String accountNumber;

    public CreateAccountRequest() { }

    public CreateAccountRequest(String productCode, String productType, String name, int age, String accountNumber) {
        this.productCode = productCode;
        this.productType = productType;
        this.name = name;
        this.age = age;
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}