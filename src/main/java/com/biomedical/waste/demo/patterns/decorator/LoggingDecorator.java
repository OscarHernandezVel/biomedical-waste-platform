package com.biomedical.waste.demo.patterns.decorator;

import com.biomedical.waste.demo.models.Waste;
import java.time.LocalDateTime;

public class LoggingDecorator extends WasteProcessorDecorator {

    /** Creates a logging decorator around the provided waste processor. */
    public LoggingDecorator(WasteProcessor delegate) {
        super(delegate);
    }

    /** Processes the waste while logging start and end events around the delegate. */
    @Override
    public ProcessingResult process(Waste waste) {
        System.out.println("[LOG START] Processing waste " + waste.getId() + " at " + LocalDateTime.now());
        ProcessingResult result = delegate.process(waste);
        System.out.println("[LOG END] Result: " + result.getStatus());
        return result;
    }
}

