package org.ram.product.strategy;

import org.ram.product.model.Product;

import java.math.BigDecimal;

public interface ProductValidator {

    void validateOnAccountCreation(Product product, int age);

    void validateOnTransaction(Product product, BigDecimal amount);
}
