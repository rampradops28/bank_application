package org.ram.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.ram.entity.*;
import org.ram.entity.BalanceType;
import org.ram.exception.*;
import org.ram.product.factory.ProductFactory;
import org.ram.product.model.Product;
import org.ram.product.strategy.ProductValidator;
import org.ram.repository.*;
import org.ram.service.TransactionService;
import org.ram.service.PostingNumberGenerator;
import org.ram.api.dto.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @Mock
    TransactionHistoryRepository transactionHistoryRepository;

    @InjectMocks
    TransactionService transactionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // DEBIT SUCCESS 
    @Test
    void debitTransaction_success() {

        Account account = new Account();
        account.setAccountNumber("ACC001");
        account.setProductType("SALARY");
        account.setProductCode("SAL1");

        Balance ledger = new Balance();
        ledger.setBalanceType(BalanceType.LEDGER);
        ledger.setBalance(new BigDecimal("500"));
        ledger.setAccount(account);

        Balance available = new Balance();
        available.setBalanceType(BalanceType.AVAILABLE);
        available.setBalance(new BigDecimal("500"));
        available.setAccount(account);

        when(accountRepository.findByAccountNumber("ACC001"))
                .thenReturn(Optional.of(account));

        when(balanceRepository.findByAccountAndType(account, BalanceType.LEDGER))
                .thenReturn(Optional.of(ledger));

        when(balanceRepository.findByAccountAndType(account, BalanceType.AVAILABLE))
                .thenReturn(Optional.of(available));

        when(productFactory.getProduct("SALARY", "SAL1"))
                .thenReturn(product);

        when(productFactory.getValidator("SALARY"))
                .thenReturn(productValidator);

        doNothing().when(productValidator)
                .validateOnTransaction(any(), any());

        when(postingNumberGenerator.generate())
                .thenReturn("POST001");

        when(postingRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        PostTransactionRequest request = new PostTransactionRequest();
        request.setAccountNumber("ACC001");
        request.setPaymentType("DEBIT");
        request.setAmount(new BigDecimal("100"));
        request.setDate(LocalDate.now());

        PostTransactionResponse response =
                transactionService.postTransaction(request);

        assertEquals("POST001", response.getPostingNumber());
        assertEquals(new BigDecimal("400"), ledger.getBalance());
        assertEquals(new BigDecimal("400"), available.getBalance());
    }

    // INSUFFICIENT FUNDS 
    @Test
    void debitTransaction_insufficientFunds() {

        Account account = new Account();
        account.setAccountNumber("ACC002");
        account.setProductType("SALARY");
        account.setProductCode("SAL1");

        Balance ledger = new Balance();
        ledger.setBalanceType(BalanceType.LEDGER);
        ledger.setBalance(new BigDecimal("50"));

        Balance available = new Balance();
        available.setBalanceType(BalanceType.AVAILABLE);
        available.setBalance(new BigDecimal("50"));

        when(accountRepository.findByAccountNumber("ACC002"))
                .thenReturn(Optional.of(account));

        when(balanceRepository.findByAccountAndType(account, BalanceType.LEDGER))
                .thenReturn(Optional.of(ledger));

        when(balanceRepository.findByAccountAndType(account, BalanceType.AVAILABLE))
                .thenReturn(Optional.of(available));

        when(productFactory.getProduct(any(), any()))
                .thenReturn(product);

        when(productFactory.getValidator(any()))
                .thenReturn(productValidator);

        doNothing().when(productValidator)
                .validateOnTransaction(any(), any());

        PostTransactionRequest request = new PostTransactionRequest();
        request.setAccountNumber("ACC002");
        request.setPaymentType("DEBIT");
        request.setAmount(new BigDecimal("100"));

        assertThrows(
                InsufficientBalanceException.class,
                () -> transactionService.postTransaction(request)
        );
    }

    // AMOUNT RULE VIOLATION 
    @Test
    void transaction_amountRuleViolation() {

        Account account = new Account();
        account.setAccountNumber("ACC003");
        account.setProductType("STUDENT");
        account.setProductCode("STU1");

        Balance ledger = new Balance();
        ledger.setBalanceType(BalanceType.LEDGER);
        ledger.setBalance(new BigDecimal("1000"));

        Balance available = new Balance();
        available.setBalanceType(BalanceType.AVAILABLE);
        available.setBalance(new BigDecimal("1000"));

        when(accountRepository.findByAccountNumber("ACC003"))
                .thenReturn(Optional.of(account));

        when(balanceRepository.findByAccountAndType(account, BalanceType.LEDGER))
                .thenReturn(Optional.of(ledger));

        when(balanceRepository.findByAccountAndType(account, BalanceType.AVAILABLE))
                .thenReturn(Optional.of(available));

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

    // POSTING NUMBER 
    @Test
    void postingNumberGeneratedCorrectly() {

        Account account = new Account();
        account.setAccountNumber("ACC004");
        account.setProductType("SALARY");
        account.setProductCode("SAL1");

        Balance ledger = new Balance();
        ledger.setBalanceType(BalanceType.LEDGER);
        ledger.setBalance(new BigDecimal("200"));

        Balance available = new Balance();
        available.setBalanceType(BalanceType.AVAILABLE);
        available.setBalance(new BigDecimal("200"));

        when(accountRepository.findByAccountNumber("ACC004"))
                .thenReturn(Optional.of(account));

        when(balanceRepository.findByAccountAndType(account, BalanceType.LEDGER))
                .thenReturn(Optional.of(ledger));

        when(balanceRepository.findByAccountAndType(account, BalanceType.AVAILABLE))
                .thenReturn(Optional.of(available));

        when(productFactory.getProduct("SALARY", "SAL1"))
                .thenReturn(product);

        when(productFactory.getValidator("SALARY"))
                .thenReturn(productValidator);

        doNothing().when(productValidator)
                .validateOnTransaction(any(), any());

        when(postingNumberGenerator.generate())
                .thenReturn("POST20251210-01");

        when(postingRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        PostTransactionRequest request = new PostTransactionRequest();
        request.setAccountNumber("ACC004");
        request.setPaymentType("CREDIT");
        request.setAmount(new BigDecimal("50"));

        PostTransactionResponse response =
                transactionService.postTransaction(request);

        assertEquals("POST20251210-01", response.getPostingNumber());
        assertEquals(new BigDecimal("250"), ledger.getBalance());
        assertEquals(new BigDecimal("250"), available.getBalance());
    }
}
