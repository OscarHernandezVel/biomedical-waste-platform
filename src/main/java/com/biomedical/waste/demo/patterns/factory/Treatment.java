package com.biomedical.waste.demo.patterns.factory;

import com.biomedical.waste.demo.models.Waste;

public abstract class Treatment {
    protected String treatmentName;
    protected int durationMinutes;

    /** Executes the treatment process on the given waste and returns a description. */
    public abstract String execute(Waste waste);

    /** Returns the name of this treatment method. */
    public abstract String getTreatmentName();

    /** Generates a formatted treatment report for regulatory records. */
    public String generateReport(Waste waste) {
        return "TREATMENT REPORT | Waste: " + waste.getId()
            + " | Type: " + waste.getType()
            + " | Treatment: " + treatmentName
            + " | Duration: " + durationMinutes + " min";
    }
}

