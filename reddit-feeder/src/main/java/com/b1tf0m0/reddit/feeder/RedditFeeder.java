package com.b1tf0m0.reddit.feeder;
import com.b1tf0m0.common.feeder.Feeder;
import com.b1tf0m0.reddit.feeder.fetch.FetchRedditEvent;

public class RedditFeeder implements Feeder {
    private final String subreddit;
    private static final String TOPIC_NAME = "RedditPost";
    private final String SOURCE_SYSTEM;

    public RedditFeeder(String subreddit) {
        this.subreddit = subreddit;
        this.SOURCE_SYSTEM = "RedditFeeder-" + subreddit;
    }

    @Override
    public void feederDataFetchAndSave() {
        FetchRedditEvent fetcher = new FetchRedditEvent();
        fetcher.fetchFeederEvent(subreddit, SOURCE_SYSTEM, TOPIC_NAME);
    }
}