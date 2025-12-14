package org.ram.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.ram.entity.TransactionHistory;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@ApplicationScoped
public class TransactionHistoryRepository implements PanacheRepository<TransactionHistory> {

    public List<TransactionHistory> findByAccountNumber(String accountNumber) {

        if(accountNumber == null){
            return List.of();
        }

        return find("accountNumber = ?1 order by transactionDate desc", accountNumber).list();

    }
}
