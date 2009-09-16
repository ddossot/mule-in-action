package com.clood.billing;


/**
 * RuntimeException wrapper for billing database exceptions
 */
public class BillingDatabaseException extends RuntimeException {
    public BillingDatabaseException() {
    }

    public BillingDatabaseException(String message) {
        super(message);
    }

    public BillingDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BillingDatabaseException(Throwable cause) {
        super(cause);
    }
}
