package com.biomedical.waste.demo.patterns.bridge;

public interface NotificationChannel {

    /** Sends a message through this channel to the given recipient. */
    void send(String message, String recipient);
}

