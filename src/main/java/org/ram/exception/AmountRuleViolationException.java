package org.ram.exception;

public class AmountRuleViolationException extends RuntimeException{
    public AmountRuleViolationException(String message) {
        super(message);
    }
}
