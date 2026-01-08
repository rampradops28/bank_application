package org.ram.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.ram.api.dto.PostTransactionRequest;
import org.ram.api.dto.PostTransactionResponse;
import org.ram.entity.*;
import org.ram.exception.AccountNotFoundException;
import org.ram.exception.AmountRuleViolationException;
import org.ram.exception.InsufficientBalanceException;
import org.ram.exception.PostingNotFoundException;
import org.ram.product.factory.ProductFactory;
import org.ram.product.model.Product;
import org.ram.product.strategy.ProductValidator;
import org.ram.repository.AccountRepository;
import org.ram.repository.BalanceRepository;
import org.ram.repository.PostingRepository;
import org.ram.repository.TransactionHistoryRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

@ApplicationScoped
public class TransactionService {

    @Inject
    AccountRepository accountRepository;

    @Inject
    BalanceRepository balanceRepository;

    @Inject
    PostingRepository postingRepository;

    @Inject
    TransactionHistoryRepository transactionHistoryRepository;

    @Inject
    ProductFactory productFactory;

    @Inject
    PostingNumberGenerator postingNumberGenerator;
 
    @Transactional
    public PostTransactionResponse postTransaction(PostTransactionRequest req) {

        // find account
        Account acc = accountRepository.findByAccountNumber(req.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        // find balance
        Balance ledger = balanceRepository
                .findByAccountAndType(acc, BalanceType.LEDGER)
                .orElseThrow();

        Balance available = balanceRepository
                .findByAccountAndType(acc, BalanceType.AVAILABLE)
                .orElseThrow();

        // get product and validator
        Product product = productFactory.getProduct(acc.getProductType(), acc.getProductCode());
        ProductValidator validator = productFactory.getValidator(acc.getProductType());

        // validate amount rule
        validator.validateOnTransaction(product, req.getAmount());

        BigDecimal balanceBefore = available.getBalance();
        // debit or credit logic
        if (req.getPaymentType().equalsIgnoreCase("DEBIT")) {

            if (available.getBalance().compareTo(req.getAmount()) < 0) { // -1
                throw new InsufficientBalanceException("Not enough balance");
            }

            /**
             * returns -1 if first < second
                returns 0 if equal
                returns 1 if first > second
             */

            ledger.setBalance(ledger.getBalance().subtract(req.getAmount()));
            available.setBalance(available.getBalance().subtract(req.getAmount()));

        } else if (req.getPaymentType().equalsIgnoreCase("CREDIT")) {
            ledger.setBalance(ledger.getBalance().add(req.getAmount()));
            available.setBalance(available.getBalance().add(req.getAmount()));
        } else {
            throw new AmountRuleViolationException("Invalid paymentType (DEBIT/CREDIT only)");
        }

        BigDecimal balanceAfter = available.getBalance();

        balanceRepository.save(ledger);
        balanceRepository.save(available);

        // create posting
        Posting posting = new Posting();
        posting.setPostingNumber(postingNumberGenerator.generate());
        posting.setPostingDate(LocalDateTime.now());
        posting.setAccount(acc);
        posting.setAccountNumber(acc.getAccountNumber());
        posting.setPaymentType(req.getPaymentType());
        posting.setAmount(req.getAmount());

        postingRepository.save(posting);

        TransactionHistory history = new TransactionHistory();
        history.setAccount(acc);
        history.setPostingNumber(posting.getPostingNumber()); 
        history.setAccountNumber(req.getAccountNumber());
        history.setPaymentType(req.getPaymentType());
        history.setAmount(req.getAmount());
        history.setBalanceBefore(balanceBefore);
        history.setBalanceAfter(balanceAfter);
        history.setTransactionDate(LocalDate.now()); 

        transactionHistoryRepository.persist(history);

        return new PostTransactionResponse(
                posting.getPostingNumber(),
                posting.getAccountNumber(),
                posting.getPaymentType(),
                posting.getAmount()
        );
    } 
    
    public PostTransactionResponse getTransaction(String postingNumber) {

    Posting posting = postingRepository.findByPostingNumber(postingNumber)
            .orElseThrow(() -> new PostingNotFoundException("Posting not found"));

    return new PostTransactionResponse(
            posting.getPostingNumber(),
            posting.getAccountNumber(),
            posting.getPaymentType(),
            posting.getAmount()
    );
}
}
