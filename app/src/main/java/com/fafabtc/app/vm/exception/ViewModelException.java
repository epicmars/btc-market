package com.fafabtc.app.vm.exception;

/**
 * Created by jastrelax on 2018/1/10.
 */

public class ViewModelException extends Exception {

    public ViewModelException() {
        super();
    }

    public ViewModelException(String message) {
        super(message);
    }

    public ViewModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ViewModelException(Throwable cause) {
        super(cause);
    }

    protected ViewModelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
