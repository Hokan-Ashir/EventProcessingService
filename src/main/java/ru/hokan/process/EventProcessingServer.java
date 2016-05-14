package ru.hokan.process;

import ru.hokan.common.AbstractProcessor;
import ru.hokan.common.AbstractServer;

import java.net.Socket;

public class EventProcessingServer extends AbstractServer {

    private static final String SERVER_NAME = EventProcessingServer.class.getName();

    public EventProcessingServer(int port) {
        super(port);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractProcessor createProcessor(Socket clientSocket) {
        return new EventProcessor(clientSocket, SERVER_NAME);
    }
}
