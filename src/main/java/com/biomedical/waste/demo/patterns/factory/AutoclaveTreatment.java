package com.biomedical.waste.demo.patterns.factory;

import com.biomedical.waste.demo.models.Waste;

public class AutoclaveTreatment extends Treatment {

    /** Creates an autoclave sterilization treatment configuration. */
    public AutoclaveTreatment() {
        this.treatmentName = "Autoclave Sterilization";
        this.durationMinutes = 18;
    }

    /** Executes the treatment process on the given waste and returns a description. */
    @Override
    public String execute(Waste waste) {
        return "Sterilizing waste " + waste.getId()
            + " at 134°C for 18 min. Pressure: 2 bar. Origin: " + waste.getOriginEntity();
    }

    /** Returns the name of this treatment method. */
    @Override
    public String getTreatmentName() {
        return treatmentName;
    }
}

