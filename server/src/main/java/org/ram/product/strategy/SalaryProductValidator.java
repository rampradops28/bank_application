package org.ram.product.strategy;

import org.ram.exception.AgeRuleViolationException;
import org.ram.exception.AmountRuleViolationException;
import org.ram.product.model.Product;
import org.ram.product.model.SalaryProduct;

import java.math.BigDecimal;

public class SalaryProductValidator implements ProductValidator {
    @Override
    public void validateOnAccountCreation(Product product, int age) {
        SalaryProduct p = (SalaryProduct) product;

        if(age < p.getMinAge()) {
            throw new AgeRuleViolationException(
                    "Age is below minimum required "+p.getMinAge()+" for Salary product"
            );
        }
    }

    @Override
    public void validateOnTransaction(Product product, BigDecimal amount) {
        SalaryProduct p = (SalaryProduct) product;

        if(amount.doubleValue() < p.getMinTransactionAmount()) {
            throw new AmountRuleViolationException(
              "Transaction amount is below minimum allowed "+p.getMinTransactionAmount()+" for Salary product"
            );
        }
    }
}
