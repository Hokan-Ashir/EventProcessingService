package ru.hokan.request;

import ru.hokan.common.AbstractProcessor;
import ru.hokan.common.AbstractServer;

import java.net.Socket;

public class EventRequestingServer extends AbstractServer {

    public EventRequestingServer(int port) {
        super(port);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractProcessor createProcessor(Socket clientSocket) {
        return new EventRequestsProcessor(clientSocket);
    }
}
