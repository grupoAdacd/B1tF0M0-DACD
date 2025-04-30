package com.b1tf0m0.reddit.fetch.inTimeFetcherUtils;

public class InTimeFetcherUtils {
    public String extractSubredditName(String url, String subredditName) {
        if (url.contains("/r/")) {
            String[] parts = url.split("/r/");
            if (parts.length > 1) {
                String subPart = parts[1];
                subredditName = subPart.split("/")[0];
                if (subredditName.contains("?")) {
                    subredditName = subredditName.split("\\?")[0];
                }
            }
        }
        return subredditName;
    }
}
