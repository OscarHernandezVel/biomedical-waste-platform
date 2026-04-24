package com.biomedical.waste.demo.patterns.decorator;

import com.biomedical.waste.demo.exception.WasteValidationException;
import com.biomedical.waste.demo.models.Waste;

public class ValidationDecorator extends WasteProcessorDecorator {

    /** Creates a validation decorator around the provided waste processor. */
    public ValidationDecorator(WasteProcessor delegate) {
        super(delegate);
    }

    /** Validates the waste payload and delegates processing if valid. */
    @Override
    public ProcessingResult process(Waste waste) {
        if (waste.getType() == null) {
            throw new WasteValidationException("type is required");
        }
        if (waste.getWeightKg() == null || waste.getWeightKg() <= 0) {
            throw new WasteValidationException("invalid weight");
        }
        if (waste.getOriginEntity() == null || waste.getOriginEntity().isBlank()) {
            throw new WasteValidationException("entity required");
        }
        return delegate.process(waste);
    }
}
