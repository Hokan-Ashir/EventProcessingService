package ru.hokan.request;

import org.apache.log4j.Logger;
import ru.hokan.common.AbstractProcessor;
import ru.hokan.database.DatabaseController;
import ru.hokan.database.Duration;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class EventRequestsProcessor extends AbstractProcessor {

    private static final Logger LOGGER = Logger.getLogger(EventRequestsProcessor.class);

    public EventRequestsProcessor(Socket clientSocket) {
        super(clientSocket);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        try {
            OutputStream output = getClientSocket().getOutputStream();
            int numberEventsMinute = DatabaseController.INSTANCE.selectEvents(Duration.MINUTE);
            int numberEventsHour = DatabaseController.INSTANCE.selectEvents(Duration.HOUR);
            int numberEventsDay = DatabaseController.INSTANCE.selectEvents(Duration.DAY);
            output.write(("HTTP/1.1 200 OK\n\nEventRequestsProcessor: "
                    + "\nNumber of events last minute: " + numberEventsMinute
                    + "\nNumber of events last hour: " + numberEventsHour
                    + "\nNumber of events last day: " + numberEventsDay +
                    "").getBytes());
            output.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("Cannot write message to client socket", e);
        }
    }
}
