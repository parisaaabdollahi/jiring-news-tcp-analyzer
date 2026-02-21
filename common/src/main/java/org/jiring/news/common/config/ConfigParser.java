package org.jiring.news.common.config;

public interface ConfigParser {
    static int parseInt(String s, int defaultValue, String key) {
        if (s == null || s.trim().isEmpty()) return defaultValue;
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }
    }

    static long parseLong(String s, long defaultValue, String key) {
        if (s == null || s.trim().isEmpty()) return defaultValue;
        try {
            return Long.parseLong(s.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }
    }
}