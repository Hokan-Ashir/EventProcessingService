package ru.hokan.common;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractServer implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(AbstractServer.class);
    private static final int NUMBER_OF_THREADS_IN_POOL = 125;

    private final int port;
    private ServerSocket serverSocket;
    private ExecutorService threadPool = Executors.newFixedThreadPool(NUMBER_OF_THREADS_IN_POOL);

    public AbstractServer(int port) {
        this.port = port;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        openServerSocket();
        while (true) {
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
                clientSocket.setKeepAlive(false);
                clientSocket.setSoLinger(true, 2);
                clientSocket.setReuseAddress(true);
                clientSocket.setTcpNoDelay(true);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }

            LOGGER.info("Executing process ...");
            threadPool.execute(createProcessor(clientSocket));
        }
    }

    private void openServerSocket() {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            serverSocket.setPerformancePreferences(3, 2, 1);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("Cannot open server port: " + port, e);
        }
    }

    protected abstract AbstractProcessor createProcessor(Socket clientSocket);
}
