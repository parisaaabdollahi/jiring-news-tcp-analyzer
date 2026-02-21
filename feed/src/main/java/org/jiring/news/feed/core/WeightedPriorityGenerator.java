package org.jiring.news.feed.core;

import java.util.Random;

public class WeightedPriorityGenerator {

    public static int generateWeightedPriority(Random random) {
        int totalWeight = 0;
        for (int i = 0; i <= 9; i++) {
            totalWeight += (10 - i);
        }

        int randomValue = random.nextInt(totalWeight);
        int cumulativeWeight = 0;

        for (int priority = 0; priority <= 9; priority++) {
            cumulativeWeight += (10 - priority);
            if (randomValue < cumulativeWeight) {
                return priority;
            }
        }
        return 9;
    }
}