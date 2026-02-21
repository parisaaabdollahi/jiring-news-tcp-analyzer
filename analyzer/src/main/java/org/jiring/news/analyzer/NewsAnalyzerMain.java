package org.jiring.news.analyzer;

import org.jiring.news.analyzer.config.AnalyzerConfigLoader;
import org.jiring.news.analyzer.core.ClientHandler;
import org.jiring.news.analyzer.core.PositiveHeadLineStore;
import org.jiring.news.analyzer.core.SummaryTask;
import org.jiring.news.common.config.AddressConfig;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Clock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NewsAnalyzerMain {
    private static final long DEFAULT_WINDOW_SECONDS = 10L;

    public static void main(String[] args) {
        new NewsAnalyzerMain().run(System.out, System.getProperties());
    }

    public void run(PrintStream log, java.util.Properties properties) {
        AddressConfig config = new AnalyzerConfigLoader().load(properties);
        long windowSeconds = DEFAULT_WINDOW_SECONDS;

        log.println("NewsAnalyzer listening on port " + config.port);
        log.println("Summary interval: " + (config.intervalMs / 1000) + "s, Window: " + windowSeconds + "s");

        Clock clock = Clock.systemUTC();
        PositiveHeadLineStore store = new PositiveHeadLineStore(clock, windowSeconds);
        ExecutorService clientPool = Executors.newCachedThreadPool();
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(
                new SummaryTask(store, windowSeconds, log),
                config.intervalMs ,
                config.intervalMs ,
                TimeUnit.MILLISECONDS);

        try (ServerSocket serverSocket = new ServerSocket(config.port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, store, log);
                clientPool.submit(handler);
            }
        } catch (IOException e) {
            log.println("NewsAnalyzer encountered an IO error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scheduler.shutdownNow();
            clientPool.shutdownNow();
        }
    }
}
