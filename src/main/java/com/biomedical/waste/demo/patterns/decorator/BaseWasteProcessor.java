package com.biomedical.waste.demo.patterns.decorator;

import com.biomedical.waste.demo.models.Waste;
import com.biomedical.waste.demo.patterns.factory.Treatment;
import com.biomedical.waste.demo.patterns.factory.TreatmentFactory;

public class BaseWasteProcessor implements WasteProcessor {

    /** Processes a waste by selecting the correct treatment and returning the execution result. */
    @Override
    public ProcessingResult process(Waste waste) {
        Treatment treatment = TreatmentFactory.create(waste.getType());
        waste.setTreatmentMethod(treatment.getTreatmentName());
        return ProcessingResult.success(treatment.execute(waste));
    }
}

