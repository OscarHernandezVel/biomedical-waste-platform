package com.biomedical.waste.demo.patterns.decorator;

import com.biomedical.waste.demo.models.Waste;
import com.biomedical.waste.demo.structures.TraceabilityLinkedList;
import java.time.LocalDateTime;
import java.util.List;

public class TraceabilityDecorator extends WasteProcessorDecorator {
    private final TraceabilityLinkedList<String> traceLog = new TraceabilityLinkedList<>();

    /** Creates a traceability decorator around the provided waste processor. */
    public TraceabilityDecorator(WasteProcessor delegate) {
        super(delegate);
    }

    /** Processes the waste and appends a trace entry describing the outcome. */
    @Override
    public ProcessingResult process(Waste waste) {
        ProcessingResult result = delegate.process(waste);
        traceLog.addLast(
            "Waste " + waste.getId() + " processed at " + LocalDateTime.now() + " | Status: " + result.getStatus()
        );
        return result;
    }

    /** Returns the trace log as a forward traversal from oldest to newest. */
    public List<String> getTraceLog() {
        return traceLog.traverseForward();
    }
}

