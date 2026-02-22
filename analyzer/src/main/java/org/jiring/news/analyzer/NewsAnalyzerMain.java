package org.jiring.news.analyzer;

import org.jiring.news.analyzer.config.AnalyzerConfigLoader;
import org.jiring.news.analyzer.core.ClientHandler;
import org.jiring.news.analyzer.core.PositiveHeadLineStore;
import org.jiring.news.analyzer.core.SummaryTask;
import org.jiring.news.common.config.AddressConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Clock;
import java.util.concurrent.*;

public class NewsAnalyzerMain {
    private static final long DEFAULT_WINDOW_SECONDS = 10L;
    private static final Logger log = LoggerFactory.getLogger(NewsAnalyzerMain.class);

    public static void main(String[] args) {
        new NewsAnalyzerMain().run(System.getProperties());
    }

    public void run(java.util.Properties properties) {
        AddressConfig config = new AnalyzerConfigLoader().load(properties);
        long windowSeconds = DEFAULT_WINDOW_SECONDS;

        log.info("NewsAnalyzer listening on port {}", config.port);
        log.info("Summary interval: {}s, Window: {}s", config.intervalMs / 1000, windowSeconds);

        Clock clock = Clock.systemUTC();
        PositiveHeadLineStore store = new PositiveHeadLineStore(clock, windowSeconds);
        ExecutorService clientPool = new ThreadPoolExecutor(
                10,
                50,
                60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),
                new ThreadPoolExecutor.AbortPolicy()
        );
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(
                new SummaryTask(store, windowSeconds),
                config.intervalMs,
                config.intervalMs,
                TimeUnit.MILLISECONDS);

        try (ServerSocket serverSocket = new ServerSocket(config.port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, store);
                try {
                    clientPool.submit(handler);
                } catch (RejectedExecutionException e) {
                    log.warn("Thread pool full, rejecting client {}: {}", clientSocket.getRemoteSocketAddress(), e.getMessage());
                    try {
                        clientSocket.close();
                    } catch (IOException io) {
                        log.warn("Failed to close rejected client socket: {}", io.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            log.error("NewsAnalyzer encountered an IO error", e);
        } finally {
            scheduler.shutdownNow();
            clientPool.shutdownNow();
        }
    }
}
