package com.biomedical.waste.demo.patterns.bridge;

public class ConsoleChannel implements NotificationChannel {

    /** Sends a message through this channel to the given recipient. */
    @Override
    public void send(String message, String recipient) {
        System.out.println("[CONSOLE ALERT] " + message);
    }
}

