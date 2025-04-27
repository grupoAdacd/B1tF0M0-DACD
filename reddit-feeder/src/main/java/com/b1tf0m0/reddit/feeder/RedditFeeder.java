package com.b1tf0m0.reddit.feeder;

import com.b1tf0m0.common.event.DefaultEventBuilder;
import com.b1tf0m0.common.event.EventFileSaver;
import com.b1tf0m0.common.feeder.Feeder;
import com.b1tf0m0.reddit.api.RedditApi;

public class RedditFeeder implements Feeder {

    private final String subreddit;

    public RedditFeeder(String subreddit) {
        this.subreddit = subreddit;
    }

    @Override
    public void fetchAndSaveEvent() {
        try {
            String apiUrl = String.format("https://www.reddit.com/r/%s/new.json?limit=100&t=all", subreddit);
            RedditApi redditApi = new RedditApi(apiUrl, "");
            String rawJson = redditApi.fetchInformation();

            DefaultEventBuilder eventBuilder = new DefaultEventBuilder();
            String eventJson = eventBuilder.buildEvent("RedditFeeder-" + subreddit, rawJson);

            if (eventJson != null) {
                EventFileSaver fileSaver = new EventFileSaver();
                fileSaver.saveEvent("RedditPost", "RedditFeeder-" + subreddit, eventJson);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
