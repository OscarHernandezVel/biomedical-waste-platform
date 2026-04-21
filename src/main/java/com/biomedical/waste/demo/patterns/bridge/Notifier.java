package com.biomedical.waste.demo.patterns.bridge;

public abstract class Notifier {
    protected final NotificationChannel channel;

    /** Creates a notifier that will send notifications through the given channel. */
    protected Notifier(NotificationChannel channel) {
        this.channel = channel;
    }

    /** Sends a notification message to the recipient using the configured channel. */
    public abstract void notify(String message, String recipient);
}

