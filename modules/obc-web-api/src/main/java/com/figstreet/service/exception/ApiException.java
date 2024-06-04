package com.figstreet.service.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.figstreet.core.ActionResult;

@JsonIgnoreProperties({"cause", "localizedMessage", "stackTrace", "suppressed"})
public abstract class ApiException extends RuntimeException {

    private int fHttpStatus;
    private ActionResult fActionResult;

    public ApiException(int pStatus, String pMessage) {
        super(pMessage);
        this.fHttpStatus = pStatus;
    }

    public ApiException(ActionResult pResult) {
        super(pResult.getErrorMessagesAsString());
        this.fActionResult = pResult;
    }

    public ApiException(int pStatus, ActionResult pResult) {
        super(pResult.getErrorMessagesAsString());
        this.fHttpStatus = pStatus;
        this.fActionResult = pResult;
    }

    public ApiException(int pStatus, Throwable pCause) {
        super(pCause);
        this.fHttpStatus = pStatus;
    }

    public ApiException(int pStatus, ActionResult pResult, Throwable pCause) {
        super(pResult.getErrorMessagesAsString(), pCause);
        this.fHttpStatus = pStatus;
        this.fActionResult = pResult;
    }

    public int getHttpStatus() {
        return this.fHttpStatus;
    }

    public ActionResult getActionResult() {
        return this.fActionResult;
    }

}
