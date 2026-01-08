package org.ram.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

import org.ram.api.dto.AccountResponse;
import org.ram.api.dto.BalanceResponse;
import org.ram.api.dto.CreateAccountRequest;
import org.ram.entity.Account;
import org.ram.entity.Balance;
import org.ram.entity.BalanceType;
import org.ram.exception.InvalidProductException;
import org.ram.product.factory.ProductFactory;
import org.ram.product.model.Product;
import org.ram.product.strategy.ProductValidator;
import org.ram.repository.AccountRepository;
import org.ram.repository.BalanceRepository;
import java.util.List;

@ApplicationScoped
public class AccountService {

    @Inject
    AccountRepository accountRepository;

    @Inject
    BalanceRepository balanceRepository;

    @Inject
    ProductFactory productFactory;

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest req) {

        // Validate product exists
        Product product = productFactory.getProduct(
                req.getProductType(),
                req.getProductCode()
        );

        // get correct validator
        ProductValidator validator = productFactory.getValidator(req.getProductType());

        // Validate age rule
        validator.validateOnAccountCreation(product, req.getAge());

        // check if accountNumber already exists
        if(accountRepository.existByAccountNumber(req.getAccountNumber())) {
            throw new InvalidProductException("Account number already exists");
        }

        // create account entity
        Account acc = new Account();
        acc.setAccountNumber(req.getAccountNumber());
        acc.setProductCode(req.getProductCode());
        acc.setProductType(req.getProductType());
        acc.setName(req.getName());
        acc.setAge(req.getAge());

        accountRepository.save(acc);

        // Balance balance = new Balance();
        // balance.setAccount(acc);

        Balance ledger = new Balance();
        ledger.setBalanceType(BalanceType.LEDGER);
        ledger.setBalance(BigDecimal.ZERO);
        ledger.setAccount(acc);

        Balance available = new Balance();
        available.setBalanceType(BalanceType.AVAILABLE);
        available.setBalance(BigDecimal.ZERO);
        available.setAccount(acc);

        balanceRepository.save(ledger);
        balanceRepository.save(available);


        return buildAccountResponse(acc, List.of(ledger, available));

        // balanceRepository.save(balance);

        // return new AccountResponse(
        //         acc.getAccountNumber(),
        //         acc.getProductCode(),
        //         acc.getProductType(),
        //         acc.getName(),
        //         acc.getAge(),
        //         balance.getBalance()
        // );
    }

    private AccountResponse buildAccountResponse(Account acc, List<Balance> balances) {

        List<BalanceResponse> balanceResponses = balances.stream()
                .map(b -> new BalanceResponse(
                        b.getBalanceType().name(),
                        b.getBalance()
                ))
                .toList();

        return new AccountResponse(
                acc.getAccountNumber(),
                acc.getProductCode(),
                acc.getProductType(),
                acc.getName(),
                acc.getAge(),
                balanceResponses
        );
    }


    public AccountResponse getAccount(String accountNumber) {
    // Find account
    Account acc = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new InvalidProductException("Account not found"));

    // Find balance
        List<Balance> balances = balanceRepository.findByAccount(acc);

        if (balances.isEmpty()) {
            throw new InvalidProductException("Balance records not found");
        }
        return buildAccountResponse(acc, balances);
}

public AccountResponse getAccount(String accountNumber, String productType) {

        // Fetch account
        Account acc = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new InvalidProductException("Account not found"));

        // Validate product type
        if (!acc.getProductType().equalsIgnoreCase(productType)) {
            throw new InvalidProductException("Product type mismatch for this account");
        }

        // Fetch balance
     List<Balance> balances = balanceRepository.findByAccount(acc);

     if (balances.isEmpty()) {
         throw new InvalidProductException("Balance records not found");
     }

     return buildAccountResponse(acc, balances);
    }

}
