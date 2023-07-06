package com.github.axescode.util;

import com.github.axescode.AxescodePlugin;

import java.util.Random;

public class RollRandom {
    private static final Random random = new Random();

    public static boolean roll(double percent) {
        assert percent > 0 && percent < 100;
        double cursor = random.nextDouble(100);

        AxescodePlugin.info("roll: " + cursor);

        return cursor <= percent;
    }
}
