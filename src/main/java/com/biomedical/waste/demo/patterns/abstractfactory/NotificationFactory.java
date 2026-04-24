package com.biomedical.waste.demo.patterns.abstractfactory;

import com.biomedical.waste.demo.models.AlertLevel;

public interface NotificationFactory {

    /** Creates an email notification object suitable for this alert risk level. */
    EmailNotification createEmailAlert();

    /** Creates an SMS notification object suitable for this alert risk level. */
    SmsNotification createSmsAlert();

    /** Creates a system notification object suitable for this alert risk level. */
    SystemNotification createSystemAlert();

    /** Returns the correct factory based on the alert level. */
    static NotificationFactory forLevel(AlertLevel level) {
        return level == AlertLevel.LOW ? new LowRiskAlertFactory() : new HighRiskAlertFactory();
    }
}

