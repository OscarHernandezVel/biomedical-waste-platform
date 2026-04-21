package com.biomedical.waste.demo.patterns.bridge;

public class UrgentNotifier extends Notifier {

    /** Creates an urgent notifier using the provided notification channel. */
    public UrgentNotifier(NotificationChannel channel) {
        super(channel);
    }

    /** Sends an urgent biomedical waste notification message to the recipient. */
    @Override
    public void notify(String message, String recipient) {
        channel.send("[URGENT - BIOMEDICAL WASTE] " + message, recipient);
    }
}

