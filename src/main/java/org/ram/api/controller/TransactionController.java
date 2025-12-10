package org.ram.api.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ram.api.dto.PostTransactionRequest;
import org.ram.api.dto.PostTransactionResponse;
import org.ram.service.TransactionService;

@Path("/api/v1/transactions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransactionController {

    @Inject
    TransactionService transactionService;

    @POST
    public Response postTransaction(PostTransactionRequest req) {

        PostTransactionResponse response = transactionService.postTransaction(req);

        return Response.ok(response).build();
    }
}
