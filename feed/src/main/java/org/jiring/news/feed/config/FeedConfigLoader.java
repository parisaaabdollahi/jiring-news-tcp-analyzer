package org.jiring.news.feed.config;

import org.jiring.news.common.config.AddressConfig;
import org.jiring.news.common.config.ConfigLoader;
import org.jiring.news.common.config.ConfigParser;

import java.util.Properties;

public final class FeedConfigLoader implements ConfigLoader {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 9999;
    private static final long DEFAULT_INTERVAL_MS = 500L;

    @Override
    public AddressConfig load(Properties props) {
        String host = props.getProperty("news.feed.host", DEFAULT_HOST);

        int port = ConfigParser.parseInt(props.getProperty("news.feed.port"), DEFAULT_PORT, "news.feed.port");
        long intervalMs = ConfigParser.parseLong(props.getProperty("news.feed.interval.ms"), DEFAULT_INTERVAL_MS, "news.feed.interval.ms");
        return new AddressConfig(host, port, intervalMs);
    }
}

