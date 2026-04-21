package com.biomedical.waste.demo.patterns.bridge;

public class StandardNotifier extends Notifier {

    /** Creates a standard notifier using the provided notification channel. */
    public StandardNotifier(NotificationChannel channel) {
        super(channel);
    }

    /** Sends a standard notification message to the recipient. */
    @Override
    public void notify(String message, String recipient) {
        channel.send(message, recipient);
    }
}

