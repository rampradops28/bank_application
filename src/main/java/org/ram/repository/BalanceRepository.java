package org.ram.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.ram.entity.Balance;

import java.util.Optional;

@ApplicationScoped
public class BalanceRepository implements PanacheRepository<Balance> {

    public Optional<Balance> findByAccountId(Long accountId) {
        if(accountId == null) {
            return Optional.empty();
        }
        Balance bal = find("account.id", accountId).firstResult();
        return Optional.ofNullable(bal);
    }

    public Balance save(Balance balance) {
        persistAndFlush(balance);
        return balance;
    }
}
