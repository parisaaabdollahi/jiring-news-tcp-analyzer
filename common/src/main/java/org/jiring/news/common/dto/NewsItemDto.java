package org.jiring.news.common.dto;

import lombok.Data;

@Data
public class NewsItemDto {
    private String headline;
    // up, down, rise, fall, good, bad, success, failure, high, low

    private int priority;

    public NewsItemDto() {
    }

    public NewsItemDto(String headline, int priority) {
        this.headline = headline;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "NewsItem{headline='" + headline + "', priority=" + priority + "}";
    }

}
