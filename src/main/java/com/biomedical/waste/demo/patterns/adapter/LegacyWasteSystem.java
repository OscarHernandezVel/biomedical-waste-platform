package com.biomedical.waste.demo.patterns.adapter;

import java.util.ArrayList;
import java.util.List;

public class LegacyWasteSystem {
    private final List<String> records = new ArrayList<>();

    /** Registers a legacy waste record string into the old system storage. */
    public void register(String type, double kg, String origin, String date) {
        records.add("TYPE:" + type + "|KG:" + kg + "|ORIGIN:" + origin + "|DATE:" + date);
    }

    /** Returns all stored legacy record strings as an array. */
    public String[] fetchAllRecords() {
        return records.toArray(new String[0]);
    }
}

