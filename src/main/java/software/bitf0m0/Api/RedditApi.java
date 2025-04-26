package software.bitf0m0.Api;

import software.bitf0m0.FetchUtils.DefaultFetch;
import java.util.ArrayList;

public class RedditApi extends DefaultFetch {
    private static final String base_fetch_api_url = "https://www.reddit.com/r/%s/new.json?limit=100&t=all";
    private static ArrayList<String> subreddit = new ArrayList<String>();
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
