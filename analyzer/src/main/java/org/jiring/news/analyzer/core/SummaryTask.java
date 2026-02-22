package org.jiring.news.analyzer.core;

import org.jiring.news.common.dto.NewsItemDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class SummaryTask implements Runnable {
    private final PositiveHeadLineStore store;
    private final long windowSeconds;
    private static final Logger log =
            LoggerFactory.getLogger(SummaryTask.class);

    public SummaryTask(PositiveHeadLineStore store, long windowSeconds) {
        this.store = store;
        this.windowSeconds = windowSeconds;

    }

    @Override
    public void run() {
        List<NewsItemDto> items = store.getItemsInWindow();
        int count = items.size();
        log.info("Last {}s: {} positive news items.", windowSeconds, count);

        if (count > 0) {
            List<String> topHeadlines = store.topNUniqueHeadlinesByPriority(items, 3);
            if (!topHeadlines.isEmpty()) {
                log.info("Top headlines:");
                for (int i = 0; i < topHeadlines.size(); i++) {
                    log.info("  {}. {}", i + 1, topHeadlines.get(i));
                }
            }
        }
    }
}