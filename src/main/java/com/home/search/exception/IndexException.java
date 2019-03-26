package com.home.search.exception;

/**
 * Created by soncd on 20/12/2018
 */
public class IndexException extends Exception {

    public IndexException() {

    }

    public IndexException(String message) {
        super(message);
    }

    public IndexException(String message, Throwable cause) {
        super(message, cause);
    }
}
