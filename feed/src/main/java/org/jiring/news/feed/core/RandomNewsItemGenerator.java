package org.jiring.news.feed.core;

import org.jiring.news.common.dto.NewsItemDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomNewsItemGenerator implements NewsItemGenerator {

    private final Random random;
    private final List<String> words;

    public RandomNewsItemGenerator(Random random, List<String> words) {
        this.random = random;
        this.words = words;
    }

    @Override
    public NewsItemDto generate() {

        int wordCount = 3 + random.nextInt(3);
        List<String> copyOfNewsItems = new ArrayList<>(words);
        Collections.shuffle(copyOfNewsItems, random);
        String generatedHeadLine = String.join(" ", copyOfNewsItems.subList(0, wordCount));
//todo
        return null;
    }


    private static int generateWeightedPriority() {
        //todo
        return 0;
    }
}
