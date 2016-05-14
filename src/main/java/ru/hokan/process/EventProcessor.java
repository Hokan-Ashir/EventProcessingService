package ru.hokan.process;

import org.apache.log4j.Logger;
import ru.hokan.MetaServer;
import ru.hokan.common.AbstractProcessor;
import ru.hokan.database.DatabaseController;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class EventProcessor extends AbstractProcessor {

    private static final Logger LOGGER = Logger.getLogger(EventProcessor.class);

    private final String serverText;

    public EventProcessor(Socket clientSocket, String serverMessage) {
        super(clientSocket);
        this.serverText = serverMessage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        try {
            OutputStream output = getClientSocket().getOutputStream();
            long time = System.currentTimeMillis();
            output.write(("HTTP/1.1 200 OK\n\nEventProcessor: " +
                    serverText + " - " +
                    time +
                    "").getBytes());
            output.close();
            LOGGER.info("Event processed. Time [" + time + "]");
            DatabaseController.INSTANCE.insertEvent(time);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("Cannot write message to client socket", e);
        } finally {
            try {
                getClientSocket().close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
                throw new RuntimeException("Cannot close client socket", e);
            }
        }
    }
}