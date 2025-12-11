package org.ram.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.ram.entity.Account;

import java.util.Optional;

@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account> {


    public Optional<Account> findByAccountNumber(String accountNumber) {
        if(accountNumber == null) {
            return Optional.empty();
        }
        Account acc = find("accountNumber", accountNumber).firstResult();
        return Optional.ofNullable(acc);
    }

    public boolean existByAccountNumber(String accountNumber) {
        if(accountNumber == null) {
            return false;
        }

        // ?1 - first parameter you pass to the count method
        return count("accountNumber = ?1", accountNumber) > 0;
    }

    public Account save(Account account) {

        // save the entity and write it to the database immediately
        persistAndFlush(account);
        return account;
    }
}
