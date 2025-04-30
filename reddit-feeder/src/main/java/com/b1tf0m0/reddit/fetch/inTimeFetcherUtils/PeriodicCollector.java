package com.b1tf0m0.reddit.fetch.inTimeFetcherUtils;

import java.util.Timer;
import java.util.TimerTask;

import com.b1tf0m0.reddit.api.RedditApi;

public class PeriodicCollector extends RedditApi {
    private final Timer timer;
    private final int intervalMinutes;
    private String subredditName;

    public PeriodicCollector(String api_url, String addition, int intervalMinutes) {
        super(api_url, addition);
        this.intervalMinutes = intervalMinutes;
        this.timer = new Timer();
        String url = api_url;
        InTimeFetcherUtils extractor = new InTimeFetcherUtils();
        this.subredditName = extractor.extractSubredditName(url, subredditName);
    }

    @Override
    public String fetchInformation() {
        String result = super.fetchInformation();
        System.out.println("Datos obtenidos de r/" + subredditName + " a las " + new java.util.Date());
        return result;
    }

    public void startPeriodicCollection() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                fetchInformation();
            }
        }, 0, intervalMinutes * 60 * 1000);
    }
}
