package com.biomedical.waste.demo.patterns.decorator;

import java.time.LocalDateTime;

public class ProcessingResult {
    private final String status;
    private final String details;
    private final LocalDateTime processedAt;

    private ProcessingResult(String status, String details, LocalDateTime processedAt) {
        this.status = status;
        this.details = details;
        this.processedAt = processedAt;
    }

    /** Creates a successful processing result with the provided details. */
    public static ProcessingResult success(String details) {
        return new ProcessingResult("SUCCESS", details, LocalDateTime.now());
    }

    /** Creates a failed processing result with the provided details. */
    public static ProcessingResult failure(String details) {
        return new ProcessingResult("FAILED", details, LocalDateTime.now());
    }

    /** Returns the result status string. */
    public String getStatus() {
        return status;
    }

    /** Returns the result details string. */
    public String getDetails() {
        return details;
    }

    /** Returns the timestamp when processing completed. */
    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    /** Returns a formatted representation of this processing result. */
    @Override
    public String toString() {
        return "ProcessingResult{status='" + status + "', details='" + details + "', processedAt=" + processedAt + '}';
    }
}

