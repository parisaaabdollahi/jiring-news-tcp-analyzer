package org.jiring.news.common.config;

public final class AddressConfig {

    public final String host;
    public final int port;
    public final long intervalMs;

    public AddressConfig(String host, int port, long intervalMs) {
        this.host = host;
        this.port = port;
        this.intervalMs = intervalMs;
    }


    @Override
    public String toString() {
        return "AddressConfig{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", intervalMs=" + intervalMs +
                '}';
    }
}
