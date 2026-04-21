package com.biomedical.waste.demo.patterns.bridge;

public class EmailChannel implements NotificationChannel {

    /** Sends a message through this channel to the given recipient. */
    @Override
    public void send(String message, String recipient) {
        System.out.println("EMAIL to " + recipient + ": " + message);
    }
}

