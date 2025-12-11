package org.ram.product.strategy;

import org.ram.exception.AgeRuleViolationException;
import org.ram.exception.AmountRuleViolationException;
import org.ram.product.model.Product;
import org.ram.product.model.StudentProduct;

import java.math.BigDecimal;

public class StudentProductValidator implements ProductValidator{
    @Override
    public void validateOnAccountCreation(Product product, int age) {
        StudentProduct p = (StudentProduct) product;

        if (age > p.getMaxAge()) {
            throw new AgeRuleViolationException(
                    "Age is above maximum allowed (" + p.getMaxAge() + ") for Student product"
            );
        }
    }

    @Override
    public void validateOnTransaction(Product product, BigDecimal amount) {
        StudentProduct p = (StudentProduct) product;

        if (amount.doubleValue() > p.getMaxTransactionAmount()) {
            throw new AmountRuleViolationException(
                    "Transaction amount exceeds max allowed (" + p.getMaxTransactionAmount() + ") for Student product"
            );
        }
    }
}
