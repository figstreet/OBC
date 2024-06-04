package com.figstreet.core;

public class RecordNotExistException extends Exception {

    public RecordNotExistException(String pMessage) {
        super(pMessage);
    }

    public RecordNotExistException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
    }

}
