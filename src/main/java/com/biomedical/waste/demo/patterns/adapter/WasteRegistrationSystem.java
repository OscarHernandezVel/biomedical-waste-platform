package com.biomedical.waste.demo.patterns.adapter;

import com.biomedical.waste.demo.models.Waste;
import java.util.List;

public interface WasteRegistrationSystem {

    /** Registers a waste item into the registration system. */
    void registerWaste(Waste waste);

    /** Returns the registered record strings from the underlying system. */
    List<String> getRegisteredIds();
}

