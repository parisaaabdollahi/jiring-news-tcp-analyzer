package org.jiring.news.common.config;

import java.util.Properties;

public interface ConfigLoader {
    AddressConfig load(Properties props);
}