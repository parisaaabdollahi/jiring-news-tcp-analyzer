package org.jiring.news.analyzer.core;

import org.jiring.news.common.dto.NewsItemDto;

import java.io.PrintStream;
import java.util.List;

public final class SummaryTask implements Runnable {
    private final PositiveHeadLineStore store;
    private final long windowSeconds;
    private final PrintStream out;

    public SummaryTask(PositiveHeadLineStore store, long windowSeconds, PrintStream out) {
        this.store = store;
        this.windowSeconds = windowSeconds;
        this.out = out;
    }

    @Override
    public void run() {
        List<NewsItemDto> items = store.getItemsInWindow();
        int count = items.size();
        out.println("Last " + windowSeconds + "s: " + count + " positive news items.");

        if (count > 0) {
            List<String> topHeadlines = store.topNUniqueHeadlinesByPriority(items,3);
            if (!topHeadlines.isEmpty()) {
                out.println("Top headlines:");
                for (int i = 0; i < topHeadlines.size(); i++) {
                    out.println("  " + (i + 1) + ". " + topHeadlines.get(i));
                }
            }
        }
    }
}