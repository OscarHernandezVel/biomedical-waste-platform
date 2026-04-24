package com.biomedical.waste.demo.exception;

public class WasteNotFoundException extends RuntimeException {

    /** Creates an exception indicating that a waste record with the given id was not found. */
    public WasteNotFoundException(String id) {
        super("Waste not found with id: " + id);
    }
}

