package org.ram.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.ram.entity.Account;
import org.ram.entity.Balance;
import org.ram.entity.BalanceType;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BalanceRepository implements PanacheRepository<Balance> {

    public List<Balance> findByAccount(Account account) {

        if (account == null) {
            return List.of();
        }

        return find("account", account).list();
    }


    public Optional<Balance> findByAccountAndType(
            Account account,
            BalanceType balanceType) {

        if (account == null || balanceType == null) {
            return Optional.empty();
        }

        Balance balance = find(
                "account = ?1 and balanceType = ?2",
                account,
                balanceType
        ).firstResult();

        return Optional.ofNullable(balance);
    }


    /**
     * Save or update balance
     */
    public Balance save(Balance balance) {
        persistAndFlush(balance);
        return balance;
    }
}
