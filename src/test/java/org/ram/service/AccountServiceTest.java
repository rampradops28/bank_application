//package org.ram.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.ram.api.dto.CreateAccountRequest;
//import org.ram.entity.Account;
//import org.ram.entity.Balance;
//import org.ram.exception.*;
//import org.ram.product.factory.ProductFactory;
//import org.ram.product.model.Product;
//import org.ram.product.strategy.ProductValidator;
//import org.ram.repository.AccountRepository;
//import org.ram.repository.BalanceRepository;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class AccountServiceTest {
//
//    @Mock AccountRepository accountRepository;
//    @Mock BalanceRepository balanceRepository;
//    @Mock ProductFactory productFactory;
//    @Mock ProductValidator validator;
//    @Mock Product product;
//
//    @InjectMocks AccountService accountService;
//
//    @BeforeEach
//    void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    // ✔ SUCCESS
//    @Test
//    void testCreateAccountSuccess() {
//        CreateAccountRequest req = new CreateAccountRequest();
//        req.accountNumber = "ACC100";
//        req.name = "John";
//        req.age = 30;
//        req.productCode = "SAL1";
//        req.productType = "SALARY";
//
//        when(accountRepository.findByAccountNumber("ACC100"))
//                .thenReturn(Optional.empty());
//
//        when(productFactory.getProduct("SALARY", "SAL1"))
//                .thenReturn(product);
//
//        when(product.getValidator()).thenReturn(validator);
//
//        doNothing().when(validator).validateAge(anyInt());
//
//        Account saved = new Account();
//        saved.id = 1L;
//
//        when(accountRepository.persist(any(Account.class))).thenReturn(saved);
//
//        Balance balance = new Balance();
//        balance.id = 1L;
//
//        when(balanceRepository.persist(any(Balance.class))).thenReturn(balance);
//
//        var result = accountService.createAccount(req);
//
//        assertEquals("ACC100", result.accountNumber);
//        verify(validator).validateAge(30);
//    }
//
//    // ❌ DUPLICATE ACCOUNT
//    @Test
//    void testDuplicateAccountNumber() {
//
//        when(accountRepository.findByAccountNumber("ACC100"))
//                .thenReturn(Optional.of(new Account()));
//
//        CreateAccountRequest req = new CreateAccountRequest();
//        req.accountNumber = "ACC100";
//
//        assertThrows(
//                DuplicateAccountException.class,
//                () -> accountService.createAccount(req)
//        );
//    }
//
//    // ❌ AGE FAILURE
//    @Test
//    void testAgeValidationFailure() {
//        CreateAccountRequest req = new CreateAccountRequest();
//        req.accountNumber = "ACC999";
//        req.age = 17;
//        req.productCode = "SAL1";
//        req.productType = "SALARY";
//
//        when(accountRepository.findByAccountNumber("ACC999"))
//                .thenReturn(Optional.empty());
//
//        when(productFactory.getProduct("SALARY", "SAL1"))
//                .thenReturn(product);
//
//        when(product.getValidator()).thenReturn(validator);
//
//        doThrow(new AgeRuleViolationException("Min age is 21"))
//                .when(validator).validateAge(17);
//
//        assertThrows(
//                AgeRuleViolationException.class,
//                () -> accountService.createAccount(req)
//        );
//    }
//}
