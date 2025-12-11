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

@Path("/api/v1/accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountController {

    @Inject
    AccountService accountService;

    @POST
    public Response createAccount(CreateAccountRequest req) {

        AccountResponse response = accountService.createAccount(req);

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

//    @GET
//    @Path("/{id}")
//    public Response getAccountById(@PathParam("id") Long id) {
//
//        AccountResponse response = accountService.getAccountById(id);
//
//        return Response.ok(response).build();
//    }

}
