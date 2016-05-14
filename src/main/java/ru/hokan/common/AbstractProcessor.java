package ru.hokan.common;

import java.net.Socket;

public abstract class AbstractProcessor implements Runnable {

    private final Socket clientSocket;

    public AbstractProcessor(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
