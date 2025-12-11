package org.ram.api.controller.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.ram.api.dto.ErrorResponse;

import java.time.LocalDateTime;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(RuntimeException ex) {

        ErrorResponse err = new ErrorResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(err)
                .build();


    }
}
