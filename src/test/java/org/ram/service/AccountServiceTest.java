package org.ram.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.ram.api.dto.CreateAccountRequest;
import org.ram.entity.Account;
import org.ram.entity.Balance;
import org.ram.exception.AgeRuleViolationException;
import org.ram.exception.InvalidProductException;
import org.ram.product.factory.ProductFactory;
import org.ram.product.model.Product;
import org.ram.product.strategy.ProductValidator;
import org.ram.repository.AccountRepository;
import org.ram.repository.BalanceRepository;

import java.math.BigDecimal; 

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;

    @Mock
    BalanceRepository balanceRepository;

    @Mock
    ProductFactory productFactory;

    @Mock
    Product product;

    @Mock
    ProductValidator productValidator;

    @InjectMocks
    AccountService accountService;
    /**
     * AccountService
        - fake AccountRepository
        - fake BalanceRepository
        - fake ProductFactory
        - fake ProductValidator
     */

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Account creation success
    @Test
    void createAccount_success() {

        CreateAccountRequest req = new CreateAccountRequest();
        req.setAccountNumber("ACC001");
        req.setAge(30);
        req.setProductType("SALARY");
        req.setProductCode("SAL1");
        req.setName("Ram");

        when(productFactory.getProduct("SALARY", "SAL1"))
                .thenReturn(product);

        when(productFactory.getValidator("SALARY"))
                .thenReturn(productValidator);

        doNothing().when(productValidator)
                .validateOnAccountCreation(product, 30);

        when(accountRepository.existByAccountNumber("ACC001"))
                .thenReturn(false);

        when(balanceRepository.save(any(Balance.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        assertDoesNotThrow(() -> accountService.createAccount(req));

        verify(accountRepository).save(any(Account.class));

        // ✅ TWO balances created: LEDGER + AVAILABLE
        verify(balanceRepository, times(2)).save(any(Balance.class));
    }

    // Duplicate account number
    @Test
    void createAccount_duplicateAccountNumber() {

        CreateAccountRequest req = new CreateAccountRequest();
        req.setAccountNumber("ACC001");
        req.setAge(30);
        req.setProductType("SALARY");
        req.setProductCode("SAL1");

        when(productFactory.getProduct(req.getProductType(), req.getProductCode()))
                .thenReturn(product);

        when(productFactory.getValidator(req.getProductType()))
                .thenReturn(productValidator);

        doNothing().when(productValidator)
                .validateOnAccountCreation(any(), anyInt());

        when(accountRepository.existByAccountNumber("ACC001"))
                .thenReturn(true);

        assertThrows(
                InvalidProductException.class,
                () -> accountService.createAccount(req)
        );

        verify(accountRepository, never()).save(any());
    }

    //  Age validation failure
    @Test
    void createAccount_ageValidationFailed() {

        CreateAccountRequest req = new CreateAccountRequest();
        req.setAccountNumber("ACC002");
        req.setAge(17);
        req.setProductType("SALARY");
        req.setProductCode("SAL1");

        when(productFactory.getProduct("SALARY", "SAL1"))
                .thenReturn(product);

        when(productFactory.getValidator("SALARY"))
                .thenReturn(productValidator);

        doThrow(new AgeRuleViolationException("Age not allowed"))
                .when(productValidator)
                .validateOnAccountCreation(product, 17);

        assertThrows(
                AgeRuleViolationException.class,
                () -> accountService.createAccount(req)
        );

        verify(accountRepository, never()).save(any());
    }
}
