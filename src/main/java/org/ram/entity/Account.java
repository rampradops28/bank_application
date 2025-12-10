package org.ram.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column (name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @Column (nullable = false)
    private String productCode;

    @Column (nullable = false)
    private String productType;

    @Column (nullable = false)
    private String name;

    @Column (nullable = false)
    private int age;

    @Column (name = "created_at")
    private Instant createdAt = Instant.now();

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private Balance balance;

    public long getId() {
        return id;}

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

    public Instant getCreatedAt() {
        return createdAt;
    }
    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }
}