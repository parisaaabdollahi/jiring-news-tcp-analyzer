package org.jiring.news.analyzer.core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class HeadlineClassifier {

    private static final Set<String> POSITIVE_HEADLINE =
            new HashSet<>(Arrays.asList("up", "rise", "good", "success", "high"));


    private HeadlineClassifier() {
    }


    public static boolean isPositive(String headline) {
        if (headline == null) return false;

        String trimmedHeadline = headline.trim();
        if (trimmedHeadline.isEmpty()) return false;

        String[] headlineWords = trimmedHeadline.toLowerCase(Locale.ROOT).split("[^a-z]+");

        int totalWordLength = headlineWords.length;
        int positiveWordsCount = 0;
        for (String word : headlineWords) {
            positiveWordsCount += POSITIVE_HEADLINE.contains(word) ? 1 : 0;
        }

        return totalWordLength != 0 && (positiveWordsCount * 2 > totalWordLength);
    }
}
