package org.ram.api.dto;

import java.io.Serializable;
import java.time.Instant;

public class ErrorResponse implements Serializable {

    public Instant timeStamp;
    public String errorCode;
    public String message;

    public ErrorResponse() {
        this.timeStamp = Instant.now();
    }

    public ErrorResponse(String errorCode, String message) {
        this.timeStamp = Instant.now();
        this.errorCode = errorCode;
        this.message = message;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}