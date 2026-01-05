package org.ram.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.ram.api.dto.PostTransactionRequest;
import org.ram.api.dto.PostTransactionResponse;
import org.ram.entity.Account;
import org.ram.entity.Balance;
import org.ram.entity.Posting;
import org.ram.exception.AmountRuleViolationException;
import org.ram.exception.InsufficientBalanceException;
import org.ram.product.factory.ProductFactory;
import org.ram.product.model.Product;
import org.ram.product.strategy.ProductValidator;
import org.ram.repository.AccountRepository;
import org.ram.repository.BalanceRepository;
import org.ram.repository.PostingRepository;
import org.ram.repository.TransactionHistoryRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    AccountRepository accountRepository;

    @Mock
    BalanceRepository balanceRepository;

    @Mock
    PostingRepository postingRepository;

    @Mock
    ProductFactory productFactory;

    @Mock
    Product product;

    @Mock
    ProductValidator productValidator;

    @Mock
    PostingNumberGenerator postingNumberGenerator;

    @InjectMocks
    TransactionService transactionService;

    @Mock
    TransactionHistoryRepository transactionHistoryRepository;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Debit success 
    @Test
    void debitTransaction_success() {

        // Account setup
        Account account = new Account();
        account.setAccountNumber("ACC001");
        account.setProductType("SALARY");
        account.setProductCode("SAL1");

        //  Balance setup
        Balance balance = new Balance();
        balance.setBalance(new BigDecimal("500"));
        balance.setAccount(account);

        // Mock Product & Validator
        Product product = mock(Product.class);
        ProductValidator validator = mock(ProductValidator.class);

        // Mock Repositories
        when(accountRepository.findByAccountNumber("ACC001"))
                .thenReturn(Optional.of(account));
        when(balanceRepository.findByAccountId(account.getId()))
                .thenReturn(Optional.of(balance));
        when(productFactory.getProduct("SALARY", "SAL1"))
                .thenReturn(product);
        when(productFactory.getValidator("SALARY"))
                .thenReturn(validator);

        // Validator does nothing
        doNothing().when(validator).validateOnTransaction(any(), any());

        // Mock Posting Number Generator
        when(postingNumberGenerator.generate())
                .thenReturn("POST001");

        // Mock PostingRepository save
        when(postingRepository.save(any(Posting.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Create request
        PostTransactionRequest request = new PostTransactionRequest();
        request.setAccountNumber("ACC001");
        request.setPaymentType("DEBIT");
        request.setAmount(new BigDecimal("100"));
        request.setDate(LocalDate.now());

        // Call service
        var response = transactionService.postTransaction(request);

        // Assertions
        assertEquals("POST001", response.getPostingNumber());
        assertEquals(new BigDecimal("400"), balance.getBalance());
    }

    // Insufficient funds
    @Test
    void debitTransaction_insufficientFunds() {

        // Account setup
        Account account = new Account();
        account.setAccountNumber("ACC002");
        account.setProductType("SALARY");
        account.setProductCode("SAL1");

        // Balance setup
        Balance balance = new Balance();
        balance.setBalance(new BigDecimal("50"));
        balance.setAccount(account);

        // Mock product and validator
        Product product = mock(Product.class);
        ProductValidator validator = mock(ProductValidator.class);

        // Mock repository methods
        when(accountRepository.findByAccountNumber("ACC002"))
                .thenReturn(Optional.of(account));
        when(balanceRepository.findByAccountId(account.getId()))
                .thenReturn(Optional.of(balance));

        // Mock factory methods
        when(productFactory.getProduct(anyString(), anyString())).thenReturn(product);
        when(productFactory.getValidator(anyString())).thenReturn(validator);

        // Make validator do nothing
        doNothing().when(validator).validateOnTransaction(any(), any());

        // Transaction request
        PostTransactionRequest request = new PostTransactionRequest();
        request.setAccountNumber("ACC002");
        request.setPaymentType("DEBIT");
        request.setAmount(new BigDecimal("100"));

        assertThrows(
                InsufficientBalanceException.class,
                () -> transactionService.postTransaction(request)
        );
    }


    @Test
    void transaction_amountRuleViolation() {

        Account account = new Account();
        account.setAccountNumber("ACC003");
        account.setProductType("STUDENT");
        account.setProductCode("STU1");

        Balance balance = new Balance();
        balance.setBalance(new BigDecimal("1000"));

        when(accountRepository.findByAccountNumber("ACC003"))
                .thenReturn(Optional.of(account));

        when(balanceRepository.findByAccountId(account.getId()))
                .thenReturn(Optional.of(balance));

        when(productFactory.getProduct("STUDENT", "STU1"))
                .thenReturn(product);

        when(productFactory.getValidator("STUDENT"))
                .thenReturn(productValidator);

        doThrow(new AmountRuleViolationException("Amount exceeds limit"))
                .when(productValidator)
                .validateOnTransaction(product, new BigDecimal("700"));

        PostTransactionRequest request = new PostTransactionRequest();
        request.setAccountNumber("ACC003");
        request.setPaymentType("DEBIT");
        request.setAmount(new BigDecimal("700"));

        assertThrows(
                AmountRuleViolationException.class,
                () -> transactionService.postTransaction(request)
        );
    }

    @Test
    void postingNumberGeneratedCorrectly() {

        // Setup Account
        Account account = new Account();
        account.setAccountNumber("ACC004");
        account.setProductType("SALARY");
        account.setProductCode("SAL1");

        // Setup Balance
        Balance balance = new Balance();
        balance.setBalance(new BigDecimal("200"));
        balance.setAccount(account);

        // Mock Product & Validator
        Product product = mock(Product.class);
        ProductValidator validator = mock(ProductValidator.class);

        // Mock Repositories
        when(accountRepository.findByAccountNumber("ACC004"))
                .thenReturn(Optional.of(account));
        when(balanceRepository.findByAccountId(account.getId()))
                .thenReturn(Optional.of(balance));
        when(productFactory.getProduct("SALARY", "SAL1"))
                .thenReturn(product);
        when(productFactory.getValidator("SALARY"))
                .thenReturn(validator);

        // Validator does nothing
        doNothing().when(validator).validateOnTransaction(any(), any());

        // Mock Posting Number Generator
        when(postingNumberGenerator.generate())
                .thenReturn("POST20251210-01");

        // Mock Posting Repository Save
        when(postingRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        // Return the same posting object passed in, so number from generator is preserved

        // Create Request
        PostTransactionRequest request = new PostTransactionRequest();
        request.setAccountNumber("ACC004");
        request.setPaymentType("CREDIT");
        request.setAmount(new BigDecimal("50"));

        // Call Service
        PostTransactionResponse response = transactionService.postTransaction(request);

        // Assert Posting Number
        assertEquals("POST20251210-01", response.getPostingNumber());

        assertEquals(new BigDecimal("250"), balance.getBalance());
    }
}
