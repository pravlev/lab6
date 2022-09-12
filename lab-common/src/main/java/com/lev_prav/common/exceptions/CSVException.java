package com.lev_prav.common.exceptions;

public class CSVException extends Exception {
    private String message;

    public CSVException(String message) {
        this.message = message;
    }

    public String getMessage() {

        return message;
    }
}
