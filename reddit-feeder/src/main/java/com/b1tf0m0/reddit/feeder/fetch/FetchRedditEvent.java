package com.b1tf0m0.reddit.feeder.fetch;

import com.b1tf0m0.common.event.DefaultEventBuilder;
import com.b1tf0m0.common.json.JSONParse;
import com.b1tf0m0.reddit.api.RedditApi;
import com.b1tf0m0.reddit.feeder.save.SaveRedditEvent;

public class FetchRedditEvent {
    public String fetchFeederEvent(String subreddit, String SOURCE_SYSTEM, String TOPIC_NAME) {
        System.out.println("Ejecutando busqueda de events para r/" + subreddit + "...");
        try {
            String apiUrl = String.format("https://www.reddit.com/r/%s/new.json?limit=100&t=all", subreddit);
            RedditApi redditApi = new RedditApi(apiUrl, "");
            String rawJson = redditApi.fetchInformation();
            JSONParse fullResponse = new JSONParse(rawJson);
            for (int i = 0; i < fullResponse.parseObject().getJSONObject("data").getJSONArray("children").length(); i++) {
                DefaultEventBuilder eventBuilder = new DefaultEventBuilder();
                String eventJsonString = eventBuilder.buildEvent(SOURCE_SYSTEM, fullResponse.parseObject().getJSONObject("data").getJSONArray("children").getJSONObject(i).getJSONObject("data").toString());
                SaveRedditEvent saver = new SaveRedditEvent();
                saver.saveFeederEvent(eventJsonString, SOURCE_SYSTEM, TOPIC_NAME);
            }
        } catch (Exception e) {
            System.err.println("Error en RedditFeeder para r/" + subreddit + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
