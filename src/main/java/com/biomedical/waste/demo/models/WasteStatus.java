package com.biomedical.waste.demo.models;

import lombok.Getter;

@Getter
public enum WasteStatus {
    PENDING("Pending", false),
    BEING_COLLECTED("Being collected", false),
    IN_TRANSPORT("In transport", false),
    BEING_TREATED("Being treated", false),
    COMPLETED("Completed", true);

    private final String displayName;
    private final boolean terminal;

    WasteStatus(String displayName, boolean terminal) {
        this.displayName = displayName;
        this.terminal = terminal;
    }

    /** Returns the next status in the workflow. */
    public WasteStatus next() {
        return switch (this) {
            case PENDING -> BEING_COLLECTED;
            case BEING_COLLECTED -> IN_TRANSPORT;
            case IN_TRANSPORT -> BEING_TREATED;
            case BEING_TREATED -> COMPLETED;
            case COMPLETED -> COMPLETED;
        };
    }
}

