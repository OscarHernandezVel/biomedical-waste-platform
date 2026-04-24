package com.biomedical.waste.demo.patterns.factory;

import com.biomedical.waste.demo.models.Waste;

public class IncinerationTreatment extends Treatment {

    /** Creates a high-temperature incineration treatment configuration. */
    public IncinerationTreatment() {
        this.treatmentName = "High-Temperature Incineration";
        this.durationMinutes = 45;
    }

    /** Executes the treatment process on the given waste and returns a description. */
    @Override
    public String execute(Waste waste) {
        double weightKg = waste.getWeightKg() == null ? 0.0 : waste.getWeightKg();
        double ashKg = weightKg * 0.1;
        return "Incinerating waste " + waste.getId() + " at 850°C. Residual ash: " + ashKg + " kg.";
    }

    /** Returns the name of this treatment method. */
    @Override
    public String getTreatmentName() {
        return treatmentName;
    }
}

