package com.biomedical.waste.demo.patterns.decorator;

import com.biomedical.waste.demo.models.Waste;

public abstract class WasteProcessorDecorator implements WasteProcessor {
    protected final WasteProcessor delegate;

    /** Creates a decorator that delegates processing to the provided waste processor. */
    protected WasteProcessorDecorator(WasteProcessor delegate) {
        this.delegate = delegate;
    }

    /** Processes the given waste by delegating to the wrapped processor. */
    @Override
    public ProcessingResult process(Waste waste) {
        return delegate.process(waste);
    }
}

