package com.biomedical.waste.demo.patterns.abstractfactory;

public interface EmailNotification {

    /** Sends an email notification to the recipient with a subject and body. */
    void send(String recipient, String subject, String body);
}

