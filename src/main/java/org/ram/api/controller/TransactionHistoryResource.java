package org.ram.api.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.ram.api.dto.TransactionHistoryResponse;
import org.ram.service.TransactionHistoryService;

import java.util.List;

@Path("/api/v1/accounts")
public class TransactionHistoryResource {

    @Inject
    TransactionHistoryService historyService;

    @GET
    @Path("/{accountNumber}/transactions")
    public List<TransactionHistoryResponse> getTransactionHistory(
            @PathParam("accountNumber") String accountNumber) {

        return historyService.getHistoryByAccountNumber(accountNumber);
    }
}
