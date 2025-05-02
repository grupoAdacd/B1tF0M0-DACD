package com.b1tf0m0.reddit.api;

import com.b1tf0m0.common.fetch.DefaultRedditFetchClasses.DefaultRedditFetch;

import java.util.ArrayList;

public class RedditApi extends DefaultRedditFetch {
    private static final String base_fetch_api_url = "https://www.reddit.com/r/%s/new.json?limit=100&t=all";
    private static ArrayList<String> subreddit = new ArrayList<>();
    public RedditApi(String api_url, String addition) {
        super(api_url, addition);
    }

    public static void main(String[] args) {
        subreddit.add("Criptocurrency");
        subreddit.add("Bitcoin");
        subreddit.add("btc");
        subreddit.add("BitcoinNews");
        for (int i = 0; i < subreddit.size(); i++) {
            String finalApiUrl = String.format(base_fetch_api_url, subreddit.get(i));
            RedditApi redditApi = new RedditApi(finalApiUrl, "");
            System.out.println(redditApi.fetchInformation());
        }
    }

    @Override
    public String fetchInformation() {
        return super.fetchInformation();
    }
}
