package com.example.lead_android2_oficina1;

import java.util.concurrent.ThreadLocalRandom;

public class OrderFetcher {

    private static final double minValue = 10.0;
    private static final double maxValue = 1000.0;

    public static double getOrder() throws InterruptedException {
        Thread.sleep(2000);
        return ThreadLocalRandom.current().nextDouble(minValue, maxValue);
    }

}
