package com.biomedical.waste.demo.patterns.abstractfactory;

public interface SmsNotification {

    /** Sends an SMS notification to the phone number with the provided message. */
    void send(String phone, String message);
}

