package org.jiring.news.analyzer.core;

import org.jiring.news.common.dto.NewsItemDto;
import org.jiring.news.common.utility.JsonLinesMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public final class ClientHandler implements Runnable {
    private final Socket socket;
    private final PositiveHeadLineStore store;

    private static final Logger log =
            LoggerFactory.getLogger(ClientHandler.class);

    public ClientHandler(Socket socket, PositiveHeadLineStore store) {
        this.socket = socket;
        this.store = store;
    }

    @Override
    public void run() {
        String remoteAddress = socket.getRemoteSocketAddress().toString();
        log.info("Client connected: {}", remoteAddress);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    NewsItemDto newsItem = JsonLinesMessageCodec.decode(line);
                    if (HeadlineClassifier.isPositive(newsItem.getHeadline())) {
                        store.addPositiveNews(newsItem);
                        log.debug("Positive news received from {}: {}", remoteAddress, newsItem);
                    } else {
                        log.debug("Negative news ignored from {}: {}", remoteAddress, newsItem);
                    }

                } catch (IOException e) {
                    log.warn("Failed to parse message from {}: {}", remoteAddress, e.getMessage());
                }
            }

        } catch (IOException e) {
            log.error("Connection error with client {}: {}", remoteAddress, e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {
                log.error("Client disconnected: {}", remoteAddress);
            }
        }
    }
}