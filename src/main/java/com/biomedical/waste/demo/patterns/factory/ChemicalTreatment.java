package com.biomedical.waste.demo.patterns.factory;

import com.biomedical.waste.demo.models.Waste;

public class ChemicalTreatment extends Treatment {

    /** Creates a chemical neutralization treatment configuration. */
    public ChemicalTreatment() {
        this.treatmentName = "Chemical Neutralization";
        this.durationMinutes = 30;
    }

    /** Executes the treatment process on the given waste and returns a description. */
    @Override
    public String execute(Waste waste) {
        double weightKg = waste.getWeightKg() == null ? 0.0 : waste.getWeightKg();
        return "Neutralizing waste " + waste.getId() + " with sodium hypochlorite. Volume: " + weightKg + " kg.";
    }

    /** Returns the name of this treatment method. */
    @Override
    public String getTreatmentName() {
        return treatmentName;
    }
}

