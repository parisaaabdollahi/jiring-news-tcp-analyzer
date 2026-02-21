package org.jiring.news.analyzer.core;

import org.jiring.news.common.dto.NewsItemDto;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PositiveHeadLineStore {

    private static final class TimedNewsItem {
        final NewsItemDto newsItem;
        final Instant seenAt;

        TimedNewsItem(NewsItemDto newsItem, Instant seenAt) {
            this.newsItem = newsItem;
            this.seenAt = seenAt;
        }
    }

    private final ConcurrentLinkedQueue<TimedNewsItem> queueItems = new ConcurrentLinkedQueue<>();
    private final Clock clock;
    private final long windowSeconds;


    public PositiveHeadLineStore(Clock clock, long windowSeconds) {
        this.clock = clock;
        this.windowSeconds = windowSeconds;
    }

    public void addPositiveNews(NewsItemDto newsItem) {
        queueItems.offer(new TimedNewsItem(newsItem, clock.instant()));
    }

    public List<NewsItemDto> getItemsInWindow() {
        Instant now = clock.instant();
        Instant cutoff = now.minusSeconds(windowSeconds);


        List<TimedNewsItem> drained = new ArrayList<>();
        TimedNewsItem item;
        while ((item = queueItems.poll()) != null) {
            drained.add(item);
        }

        List<NewsItemDto> inWindow = new ArrayList<>();
        for (TimedNewsItem timed : drained) {
            if (!timed.seenAt.isBefore(cutoff)) {
                inWindow.add(timed.newsItem);
                queueItems.offer(timed);
            }
        }

        inWindow.sort(Comparator
                .comparingInt(NewsItemDto::getPriority).reversed()
                .thenComparing(NewsItemDto::getHeadline, Comparator.nullsLast(String::compareTo)));
        return inWindow;
    }

    public List<String> getTopUniqueHeadlines(int maxCount) {
        return topNUniqueHeadlinesByPriority(getItemsInWindow(), maxCount);
    }

    public static List<String> topNUniqueHeadlinesByPriority(List<NewsItemDto> items, int n) {
        List<NewsItemDto> copy = new ArrayList<>(items);
        copy.sort(Comparator
                .comparingInt(NewsItemDto::getPriority).reversed()
                .thenComparing(NewsItemDto::getHeadline, Comparator.nullsLast(String::compareTo)));

        LinkedHashSet<String> unique = new LinkedHashSet<>();
        for (NewsItemDto item : copy) {
            if (item.getHeadline() == null) continue;
            unique.add(item.getHeadline());
            if (unique.size() >= n) break;
        }
        return new ArrayList<>(unique);
    }
}
