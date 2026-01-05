package org.ram.api.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ram.api.dto.AccountResponse;
import org.ram.api.dto.CreateAccountRequest;
import org.ram.service.AccountService;
import jakarta.ws.rs.GET; 
import jakarta.ws.rs.QueryParam;


@Path("/api/v1/accounts")
@Consumes(MediaType.APPLICATION_JSON) // accept JSON input
@Produces(MediaType.APPLICATION_JSON) // return JSON output
public class AccountController {

    @Inject
    AccountService accountService;

    @POST
    public Response createAccount(CreateAccountRequest req) {

        AccountResponse response = accountService.createAccount(req);

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    public Response getAccount(
            @QueryParam("accountNumber") String accountNumber,
            @QueryParam("productType") String productType) {

        if (accountNumber == null || productType == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("accountNumber & productType are required").build();
        }
        AccountResponse response = accountService.getAccount(accountNumber, productType);
        return Response.ok(response).build();
    } 

}
