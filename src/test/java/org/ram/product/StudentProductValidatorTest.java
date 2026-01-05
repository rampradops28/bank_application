package org.ram.product;

import org.junit.jupiter.api.Test;
import org.ram.exception.AgeRuleViolationException;
import org.ram.exception.AmountRuleViolationException;
import org.ram.product.model.StudentProduct;
import org.ram.product.strategy.StudentProductValidator;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class StudentProductValidatorTest {

    private final StudentProductValidator validator =
            new StudentProductValidator();

    @Test
    void accountCreation_success() {
        StudentProduct product = new StudentProduct();
        product.setMaxAge(18);

        assertDoesNotThrow(() ->
                validator.validateOnAccountCreation(product, 17)
        );
    }

    @Test
    void accountCreation_ageViolation() {
        StudentProduct product = new StudentProduct();
        product.setMaxAge(18);

        assertThrows(
                AgeRuleViolationException.class,
                () -> validator.validateOnAccountCreation(product, 21)
        );
    }

    @Test
    void transaction_success() {
        StudentProduct product = new StudentProduct();
        product.setMaxTransactionAmount(100);

        assertDoesNotThrow(() ->
                validator.validateOnTransaction(
                        product, new BigDecimal("50")
                )
        );
    }

    @Test
    void transaction_amountViolation() {
        StudentProduct product = new StudentProduct();
        product.setMaxTransactionAmount(100);

        assertThrows(
                AmountRuleViolationException.class,
                () -> validator.validateOnTransaction(
                        product, new BigDecimal("200")
                )
        );
    }
}
