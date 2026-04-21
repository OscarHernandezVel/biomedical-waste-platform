package com.biomedical.waste.demo.patterns.abstractfactory;

import com.biomedical.waste.demo.models.AlertLevel;

public class LowRiskAlertFactory implements NotificationFactory {

    /** Creates an email notification object suitable for this alert risk level. */
    @Override
    public EmailNotification createEmailAlert() {
        return new LowRiskEmailNotification();
    }

    /** Creates an SMS notification object suitable for this alert risk level. */
    @Override
    public SmsNotification createSmsAlert() {
        return new LowRiskSmsNotification();
    }

    /** Creates a system notification object suitable for this alert risk level. */
    @Override
    public SystemNotification createSystemAlert() {
        return new LowRiskSystemNotification();
    }

    private static final class LowRiskEmailNotification implements EmailNotification {

        /** Sends an email notification to the recipient with a subject and body. */
        @Override
        public void send(String recipient, String subject, String body) {
            System.out.println("Low risk: email notification skipped");
        }
    }

    private static final class LowRiskSmsNotification implements SmsNotification {

        /** Sends an SMS notification to the phone number with the provided message. */
        @Override
        public void send(String phone, String message) {
            System.out.println("Low risk: SMS notification skipped");
        }
    }

    private static final class LowRiskSystemNotification implements SystemNotification {

        /** Logs a system notification message tagged with the provided alert level. */
        @Override
        public void log(String message, AlertLevel level) {
            System.out.println("[SYSTEM LOW] " + level + ": " + message);
        }
    }
}
