package com.b1tf0m0.common.scheduler;

import com.b1tf0m0.common.feeder.Feeder;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DefaultFeederScheduler {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final List<Feeder> feeders;

    public DefaultFeederScheduler(List<Feeder> feeders) {
        this.feeders = feeders;
    }

    public void start(long intervalMinutes) {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                for (Feeder feeder : feeders) {
                    feeder.feederDataFetchAndSave();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, intervalMinutes, TimeUnit.MINUTES);
    }
}
