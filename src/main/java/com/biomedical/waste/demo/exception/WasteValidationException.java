package com.biomedical.waste.demo.exception;

public class WasteValidationException extends RuntimeException {

    /** Creates an exception indicating that a waste payload failed validation. */
    public WasteValidationException(String message) {
        super(message);
    }
}

