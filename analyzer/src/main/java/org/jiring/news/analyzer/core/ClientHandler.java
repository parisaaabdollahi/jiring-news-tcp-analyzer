package org.jiring.news.analyzer.core;

import org.jiring.news.common.dto.NewsItemDto;
import org.jiring.news.common.utility.JsonLinesMessageCodec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public final class ClientHandler implements Runnable {
    private final Socket socket;
    private final PositiveHeadLineStore store;
    private final PrintStream log;

    public ClientHandler(Socket socket, PositiveHeadLineStore store, PrintStream log) {
        this.socket = socket;
        this.store = store;
        this.log = log;
    }

    @Override
    public void run() {
        String remoteAddress = socket.getRemoteSocketAddress().toString();
        log.println("Client connected: " + remoteAddress);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), "UTF-8"))) {

            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    NewsItemDto newsItem = JsonLinesMessageCodec.decode(line);
                    if (HeadlineClassifier.isPositive(newsItem.getHeadline())) {
                        store.addPositiveNews(newsItem);
                    }
                } catch (IOException e) {
                    log.println("Failed to parse message from " + remoteAddress + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            log.println("Connection error with client " + remoteAddress + ": " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
            log.println("Client disconnected: " + remoteAddress);
        }
    }
}