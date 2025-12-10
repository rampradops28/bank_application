package org.ram.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.ram.api.dto.AccountResponse;
import org.ram.api.dto.CreateAccountRequest;
import org.ram.entity.Account;
import org.ram.entity.Balance;
import org.ram.exception.InvalidProductException;
import org.ram.product.factory.ProductFactory;
import org.ram.product.model.Product;
import org.ram.product.strategy.ProductValidator;
import org.ram.repository.AccountRepository;
import org.ram.repository.BalanceRepository;

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

        Balance balance = new Balance();
        balance.setAccount(acc);

        balanceRepository.save(balance);

        return new AccountResponse(
                acc.getAccountNumber(),
                acc.getProductCode(),
                acc.getProductType(),
                acc.getName(),
                acc.getAge(),
                balance.getBalance()
        );
    }
}
