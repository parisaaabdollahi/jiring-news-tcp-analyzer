package org.jiring.news.analyzer.config;

import org.jiring.news.common.config.AddressConfig;
import org.jiring.news.common.config.ConfigLoader;
import org.jiring.news.common.config.ConfigParser;

import java.util.Properties;

public class AnalyzerConfigLoader implements ConfigLoader {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 9999;
    private static final long DEFAULT_SUMMARY_INTERVAL_MS = 10000L;

    @Override
    public AddressConfig load(Properties props) {
        String host = props.getProperty("news.analyzer.host", DEFAULT_HOST);

        int port = ConfigParser.parseInt(props.getProperty("news.analyzer.port"), DEFAULT_PORT, "news.analyzer.port");
        long intervalMs = ConfigParser.parseLong(
                props.getProperty("news.analyzer.summary.interval.ms"),
                DEFAULT_SUMMARY_INTERVAL_MS,
                "news.analyzer.summary.interval.ms");

        if (intervalMs<= 0){
            intervalMs= DEFAULT_SUMMARY_INTERVAL_MS;
        }
        return new AddressConfig(host, port, intervalMs);
    }
}