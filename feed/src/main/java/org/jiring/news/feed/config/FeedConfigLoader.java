package org.jiring.news.feed.config;

import java.util.Properties;

public final class FeedConfigLoader {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 9999;
    private static final long DEFAULT_INTERVAL_MS = 500L;

    public FeedConfig load(Properties props) {
        String host = props.getProperty("news.feed.host", DEFAULT_HOST);

        int port = parseInt(props.getProperty("news.feed.port"), DEFAULT_PORT, "news.feed.port");
        long intervalMs = parseLong(props.getProperty("news.feed.interval.ms"), DEFAULT_INTERVAL_MS, "news.feed.interval.ms");
        return new FeedConfig(host, port, intervalMs);
    }

    private int parseInt(String s, int defaultValue, String key) {
        if (s == null || s.trim().isEmpty()) return defaultValue;
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private long parseLong(String s, long defaultValue, String key) {
        if (s == null || s.trim().isEmpty()) return defaultValue;
        try {
            return Long.parseLong(s.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

