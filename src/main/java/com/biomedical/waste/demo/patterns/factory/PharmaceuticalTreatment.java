package com.biomedical.waste.demo.patterns.factory;

import com.biomedical.waste.demo.models.Waste;

public class PharmaceuticalTreatment extends Treatment {

    /** Creates a pharmaceutical inactivation treatment configuration. */
    public PharmaceuticalTreatment() {
        this.treatmentName = "Pharmaceutical Inactivation";
        this.durationMinutes = 25;
    }

    /** Executes the treatment process on the given waste and returns a description. */
    @Override
    public String execute(Waste waste) {
        double weightKg = waste.getWeightKg() == null ? 0.0 : waste.getWeightKg();
        return "Inactivating pharmaceutical waste " + waste.getId() + ". Processed: " + weightKg + " kg.";
    }

    /** Returns the name of this treatment method. */
    @Override
    public String getTreatmentName() {
        return treatmentName;
    }
}

