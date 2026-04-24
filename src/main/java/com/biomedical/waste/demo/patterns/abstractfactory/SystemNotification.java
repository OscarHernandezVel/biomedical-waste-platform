package com.biomedical.waste.demo.patterns.abstractfactory;

import com.biomedical.waste.demo.models.AlertLevel;

public interface SystemNotification {

    /** Logs a system notification message tagged with the provided alert level. */
    void log(String message, AlertLevel level);
}

