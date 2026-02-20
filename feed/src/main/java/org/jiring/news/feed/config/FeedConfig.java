package org.jiring.news.feed.config;

public final class FeedConfig {

    public final String host;
    public final int port;
    public final long intervalMs;

    public FeedConfig(String host, int port, long intervalMs) {
        this.host = host;
        this.port = port;
        this.intervalMs = intervalMs;
    }


    @Override
    public String toString() {
        return "FeedConfig{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", intervalMs=" + intervalMs +
                '}';
    }
}
