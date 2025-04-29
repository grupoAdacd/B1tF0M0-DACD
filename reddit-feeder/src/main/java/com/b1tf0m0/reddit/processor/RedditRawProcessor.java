package com.b1tf0m0.reddit.processor;

import com.b1tf0m0.common.json.JSONParse;
import com.b1tf0m0.reddit.api.RedditPost;
import org.json.JSONArray;
import org.json.JSONObject;

public class RedditRawProcessor {

    public RedditPost processRawToObject(String eventJsonString) {
        if (!eventJsonString.isEmpty() && eventJsonString != null) {
            JSONParse eventParser = new JSONParse(eventJsonString);
            for (int i=0; i<100; i++) {
                JSONArray jsonArray = eventParser.parseObject().getJSONArray("children");
                JSONObject data = new JSONObject(jsonArray.get(i));
                return new RedditPost(
                        data.getString("subreddit"),
                        data.getString("selftext"),
                        data.getString("title"),
                        data.getString("permalink"),
                        data.getString("url"),
                        data.getInt("subreddit_subscribers"),
                        data.getInt("num_comments"),
                        data.getLong("created"),
                        data.getString("author"));
            }
        }
        return null;
    }

}
