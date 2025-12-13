// package org.ram.service;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.*;
// import org.ram.api.dto.PostTransactionRequest;
// import org.ram.entity.Account;
// import org.ram.entity.Balance;
// import org.ram.entity.Posting;
// import org.ram.exception.*;
// import org.ram.product.factory.ProductFactory;
// import org.ram.product.model.Product;
// import org.ram.product.strategy.ProductValidator;
// import org.ram.repository.AccountRepository;
// import org.ram.repository.BalanceRepository;
// import org.ram.repository.PostingRepository;

// import java.math.BigDecimal;
// import java.time.LocalDate;
// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// public class TransactionServiceTest {

//     @Mock AccountRepository accountRepository;
//     @Mock PostingRepository postingRepository;
//     @Mock BalanceRepository balanceRepository;

//     @Mock ProductFactory productFactory;
//     @Mock Product product;
//     @Mock ProductValidator validator;

//     @Mock PostingNumberGenerator postingNumberGenerator;

//     @InjectMocks TransactionService transactionService;

//     @BeforeEach
//     void setup() {
//         MockitoAnnotations.openMocks(this);
//     }

//     // ✔ DEBIT SUCCESS
//     @Test
//     void testDebitSuccess() {
//         Account acc = new Account();
//         acc.accountNumber = "ACC001";

//         Balance bal = new Balance();
//         bal.balance = new BigDecimal("500");

//         acc.balance = bal;

//         PostTransactionRequest req = new PostTransactionRequest();
//         req.accountNumber = "ACC001";
//         req.paymentType = "DEBIT";
//         req.amount = new BigDecimal("100");
//         req.postingDate = LocalDate.now();

//         when(accountRepository.findByAccountNumber("ACC001"))
//                 .thenReturn(Optional.of(acc));

//         when(productFactory.getProduct(acc.productType, acc.productCode))
//                 .thenReturn(product);

//         when(product.getValidator()).thenReturn(validator);

//         doNothing().when(validator).validateAmount(any(), anyString());

//         when(postingNumberGenerator.generate(any()))
//                 .thenReturn("P20250101-00001");

//         Posting savedPosting = new Posting();
//         savedPosting.postingNumber = "P20250101-00001";

//         when(postingRepository.persist(any(Posting.class)))
//                 .thenReturn(savedPosting);

//         var result = transactionService.postTransaction(req);

//         assertEquals("P20250101-00001", result.postingNumber);
//         assertEquals(new BigDecimal("400"), acc.balance.balance);
//     }

//     // ❌ INSUFFICIENT FUNDS
//     @Test
//     void testInsufficientFunds() {
//         Account acc = new Account();
//         acc.accountNumber = "ACC001";

//         Balance bal = new Balance();
//         bal.balance = new BigDecimal("50");

//         acc.balance = bal;

//         PostTransactionRequest req = new PostTransactionRequest();
//         req.accountNumber = "ACC001";
//         req.paymentType = "DEBIT";
//         req.amount = new BigDecimal("100");

//         when(accountRepository.findByAccountNumber("ACC001"))
//                 .thenReturn(Optional.of(acc));

//         assertThrows(
//                 InsufficientBalanceException.class,
//                 () -> transactionService.postTransaction(req)
//         );
//     }

//     // ❌ AMOUNT RULE FAILURE
//     @Test
//     void testAmountRuleFailure() {

//         Account acc = new Account();
//         acc.accountNumber = "ACC001";
//         acc.productType = "STUDENT";
//         acc.productCode = "STU1";
//         acc.balance = new Balance();
//         acc.balance.balance = new BigDecimal("200");

//         PostTransactionRequest req = new PostTransactionRequest();
//         req.accountNumber = "ACC001";
//         req.paymentType = "DEBIT";
//         req.amount = new BigDecimal("600");

//         when(accountRepository.findByAccountNumber("ACC001"))
//                 .thenReturn(Optional.of(acc));

//         when(productFactory.getProduct("STUDENT", "STU1"))
//                 .thenReturn(product);

//         when(product.getValidator()).thenReturn(validator);

//         doThrow(new AmountRuleViolationException("Max limit exceeded"))
//                 .when(validator).validateAmount(any(), anyString());

//         assertThrows(
//                 AmountRuleViolationException.class,
//                 () -> transactionService.postTransaction(req)
//         );
//     }

//     // ✔ POSTING NUMBER GENERATION
//     @Test
//     void testPostingNumberGeneration() {
//         Account acc = new Account();
//         acc.accountNumber = "ACC001";
//         acc.balance = new Balance();
//         acc.balance.balance = new BigDecimal("500");

//         PostTransactionRequest req = new PostTransactionRequest();
//         req.accountNumber = "ACC001";
//         req.paymentType = "CREDIT";
//         req.amount = new BigDecimal("50");
//         req.postingDate = LocalDate.of(2025, 12, 10);

//         when(accountRepository.findByAccountNumber("ACC001"))
//                 .thenReturn(Optional.of(acc));

//         when(productFactory.getProduct(any(), any()))
//                 .thenReturn(product);

//         when(product.getValidator()).thenReturn(validator);

//         when(postingNumberGenerator.generate(LocalDate.of(2025, 12, 10)))
//                 .thenReturn("P20251210-00005");

//         Posting saved = new Posting();
//         saved.postingNumber = "P20251210-00005";

//         when(postingRepository.persist(any())).thenReturn(saved);

//         var result = transactionService.postTransaction(req);

//         assertEquals("P20251210-00005", result.postingNumber);
//     }
// }
