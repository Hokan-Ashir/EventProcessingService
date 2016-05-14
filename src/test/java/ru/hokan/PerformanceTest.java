package ru.hokan;

import org.junit.Assert;
import org.junit.Test;
import ru.hokan.database.DatabaseController;
import ru.hokan.database.Duration;

import java.io.IOException;
import java.net.Socket;

public class PerformanceTest {

    @Test
    public void shouldProduceMassiveEvents() {
        MetaServer.INSTANCE.run();

        WorkerBatch batch = new WorkerBatch(100, 100);
        batch.run();

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int selectEvents = DatabaseController.INSTANCE.selectEvents(Duration.MINUTE);
        Assert.assertEquals(10000, selectEvents);
    }

    private static class WorkerBatch {
        private final int numberOfWorkers;
        private final int numberOfEvents;

        public WorkerBatch(int numberOfWorkers, int numberOfEvents) {
            this.numberOfWorkers = numberOfWorkers;
            this.numberOfEvents = numberOfEvents;
        }

        public void run() {
            for (int i = 0; i < numberOfWorkers; ++i) {
                new Thread(new Worker(numberOfEvents)).start();
            }
        }
    }

    private static class Worker implements Runnable {

        private final int numberOfEvents;

        public Worker(int numberOfEvents) {
            this.numberOfEvents = numberOfEvents;
        }

        @Override
        public void run() {
            for (int i = 0; i < numberOfEvents; ++i) {
                try {
                    Socket socket = new Socket("localhost", 9080);
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
