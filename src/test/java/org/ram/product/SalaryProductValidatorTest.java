package org.ram.product;

import org.junit.jupiter.api.Test;
import org.ram.exception.AgeRuleViolationException;
import org.ram.exception.AmountRuleViolationException;
import org.ram.product.model.SalaryProduct;
import org.ram.product.strategy.SalaryProductValidator;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SalaryProductValidatorTest {

    private final SalaryProductValidator validator =
            new SalaryProductValidator();

    @Test
    void accountCreation_success() {
        SalaryProduct product = new SalaryProduct();
        product.setMinAge(21);

        assertDoesNotThrow(() ->
                validator.validateOnAccountCreation(product, 25)
        );
    }

    @Test
    void accountCreation_ageViolation() {
        SalaryProduct product = new SalaryProduct();
        product.setMinAge(21);

        assertThrows(
                AgeRuleViolationException.class,
                () -> validator.validateOnAccountCreation(product, 18)
        );
    }

    @Test
    void transaction_success() {
        SalaryProduct product = new SalaryProduct();
        product.setMinTransactionAmount(100);

        assertDoesNotThrow(() ->
                validator.validateOnTransaction(
                        product, new BigDecimal("200")
                )
        );
    }

    @Test
    void transaction_amountViolation() {
        SalaryProduct product = new SalaryProduct();
        product.setMinTransactionAmount(100);

        assertThrows(
                AmountRuleViolationException.class,
                () -> validator.validateOnTransaction(
                        product, new BigDecimal("50")
                )
        );
    }
}
