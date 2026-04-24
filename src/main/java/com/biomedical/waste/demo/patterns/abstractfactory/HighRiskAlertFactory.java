package com.biomedical.waste.demo.patterns.abstractfactory;

import com.biomedical.waste.demo.models.AlertLevel;

public class HighRiskAlertFactory implements NotificationFactory {

    /** Creates an email notification object suitable for this alert risk level. */
    @Override
    public EmailNotification createEmailAlert() {
        return new HighRiskEmailNotification();
    }

    /** Creates an SMS notification object suitable for this alert risk level. */
    @Override
    public SmsNotification createSmsAlert() {
        return new HighRiskSmsNotification();
    }

    /** Creates a system notification object suitable for this alert risk level. */
    @Override
    public SystemNotification createSystemAlert() {
        return new HighRiskSystemNotification();
    }

    private static final class HighRiskEmailNotification implements EmailNotification {

        /** Sends an email notification to the recipient with a subject and body. */
        @Override
        public void send(String recipient, String subject, String body) {
            System.out.println("EMAIL to " + recipient + ": [HIGH RISK] " + subject + " — " + body);
        }
    }

    private static final class HighRiskSmsNotification implements SmsNotification {

        /** Sends an SMS notification to the phone number with the provided message. */
        @Override
        public void send(String phone, String message) {
            System.out.println("SMS to " + phone + ": [URGENT BIOMEDICAL] " + message);
        }
    }

    private static final class HighRiskSystemNotification implements SystemNotification {

        /** Logs a system notification message tagged with the provided alert level. */
        @Override
        public void log(String message, AlertLevel level) {
            System.out.println("[SYSTEM HIGH] " + level + ": " + message);
        }
    }
}
