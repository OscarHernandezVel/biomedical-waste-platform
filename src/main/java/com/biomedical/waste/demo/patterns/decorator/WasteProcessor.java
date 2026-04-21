package com.biomedical.waste.demo.patterns.decorator;

import com.biomedical.waste.demo.models.Waste;

public interface WasteProcessor {

    /** Processes the given waste and returns a structured processing result. */
    ProcessingResult process(Waste waste);
}

