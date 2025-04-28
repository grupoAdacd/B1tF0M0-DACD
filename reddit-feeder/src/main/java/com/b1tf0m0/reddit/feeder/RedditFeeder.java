package com.b1tf0m0.reddit.feeder;

import com.b1tf0m0.common.broker.EventPublisher;
import com.b1tf0m0.common.event.DefaultEventBuilder;
import com.b1tf0m0.common.event.EventFileSaver;
import com.b1tf0m0.common.feeder.Feeder;
import com.b1tf0m0.reddit.api.RedditApi;
import com.b1tf0m0.reddit.database.RedditEventInserter;
import org.json.JSONArray;
import org.json.JSONObject;

public class RedditFeeder implements Feeder {

    private final String subreddit;

    public RedditFeeder(String subreddit) {
        this.subreddit = subreddit;
    }

    //TODO APPLY SRP
    @Override
    public void fetchAndSaveEvent() {
        System.out.println("🔥 Ejecutando fetchAndSaveEvent en RedditFeeder...");
        try {
            String apiUrl = String.format("https://www.reddit.com/r/%s/new.json?limit=100&t=all", subreddit);
            RedditApi redditApi = new RedditApi(apiUrl, "");
            String rawJson = redditApi.fetchInformation();

            JSONObject fullResponse = new JSONObject(rawJson);
            JSONObject data = fullResponse.getJSONObject("data");
            JSONArray children = data.getJSONArray("children");

            for (int i = 0; i < children.length(); i++) {
                JSONObject postData = children.getJSONObject(i).getJSONObject("data");

                DefaultEventBuilder eventBuilder = new DefaultEventBuilder();
                String eventJsonString = eventBuilder.buildEvent("RedditFeeder-" + subreddit, postData.toString());

                if (eventJsonString != null) {
                    EventFileSaver fileSaver = new EventFileSaver();
                    fileSaver.saveEvent("RedditPost", "RedditFeeder-" + subreddit, eventJsonString);

                    JSONObject eventJson = new JSONObject(eventJsonString);
                    RedditEventInserter inserter = new RedditEventInserter();
                    inserter.insertEvent(eventJson);

                    EventPublisher.publishEvent("RedditPost", eventJsonString);
                }
            }

            System.out.println("Eventos capturados y procesados de r/" + subreddit + ": " + children.length() + " posts.");

        } catch (Exception e) {
            System.err.println("Error capturando eventos de subreddit " + subreddit + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
