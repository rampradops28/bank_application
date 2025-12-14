package org.ram.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.ram.api.dto.TransactionHistoryResponse;
import org.ram.entity.TransactionHistory;
import org.ram.repository.TransactionHistoryRepository;

import java.util.List;

@ApplicationScoped
public class TransactionHistoryService {

    @Inject
    TransactionHistoryRepository historyRepository;

    public List<TransactionHistoryResponse> getHistoryByAccountNumber(String accountNumber) {

        List<TransactionHistory> histories = historyRepository.findByAccountNumber(accountNumber);


        return histories.stream()
                .map(this::mapToDto)
                .toList();
    }

    private TransactionHistoryResponse mapToDto(TransactionHistory history) {
        TransactionHistoryResponse dto = new TransactionHistoryResponse();

        dto.setPostinNumber(history.getPostingNumber());
        dto.setPaymentType(history.getPaymentType());
        dto.setBalanceBefore(history.getBalanceBefore());
        dto.setBalanceAfter(history.getBalanceAfter());
        dto.setAmount(history.getAmount());
        dto.setTransactionDate(history.getTransactionDate());

        return dto;
    }
}
