package ru.hokan;

import ru.hokan.common.AbstractServer;
import ru.hokan.database.DatabaseController;
import ru.hokan.process.EventProcessingServer;
import ru.hokan.request.EventRequestingServer;

public enum MetaServer {
    INSTANCE;

    private static final int MESSAGES_PORT = 9080;
    private static final int REQUESTS_PORT = 9081;

    private final AbstractServer eventProcessingServer;
    private final AbstractServer requestsProcessingServer;

    MetaServer() {
        DatabaseController.INSTANCE.initialize();
        this.eventProcessingServer = new EventProcessingServer(MESSAGES_PORT);
        this.requestsProcessingServer = new EventRequestingServer(REQUESTS_PORT);
    }

    public void run() {
        new Thread(eventProcessingServer).start();
        new Thread(requestsProcessingServer).start();
    }
}
