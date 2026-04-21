package com.biomedical.waste.demo.patterns.adapter;

import com.biomedical.waste.demo.models.Waste;
import java.util.Arrays;
import java.util.List;

public class LegacySystemAdapter implements WasteRegistrationSystem {
    private final LegacyWasteSystem legacySystem = new LegacyWasteSystem();

    /** Registers a waste item by converting it into the legacy record format. */
    @Override
    public void registerWaste(Waste waste) {
        legacySystem.register(
            waste.getType().name(),
            waste.getWeightKg(),
            waste.getOriginEntity(),
            waste.getGenerationDate().toString()
        );
    }

    /** Returns the registered record strings from the underlying system. */
    @Override
    public List<String> getRegisteredIds() {
        return Arrays.asList(legacySystem.fetchAllRecords());
    }
}

