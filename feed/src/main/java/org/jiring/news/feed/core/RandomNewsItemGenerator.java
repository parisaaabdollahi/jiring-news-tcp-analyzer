package org.jiring.news.feed.core;

import org.jiring.news.common.dto.NewsItemDto;

import java.util.*;

public class RandomNewsItemGenerator {

    private static final List<String> HEADLINE_WORDS = Arrays.asList(
            "up", "down", "rise", "fall", "good", "bad", "success", "failure", "high", "low"
    );

    public static NewsItemDto generate(Random random) {
        String headline = generateHeadline(random);
        int priority = WeightedPriorityGenerator.generateWeightedPriority(random);
        return new NewsItemDto(headline, priority);
    }

    private static String generateHeadline(Random random) {
        int wordCount = 3 + random.nextInt(3);
        List<String> copyOfNewsItems = new ArrayList<>(HEADLINE_WORDS);
        Collections.shuffle(copyOfNewsItems, random);
        return String.join(" ", copyOfNewsItems.subList(0, wordCount));
    }
}
