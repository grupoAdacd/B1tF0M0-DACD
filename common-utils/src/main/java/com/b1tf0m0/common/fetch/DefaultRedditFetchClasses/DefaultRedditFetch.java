package com.b1tf0m0.common.fetch.DefaultRedditFetchClasses;

import com.b1tf0m0.common.http.DefaultHttpService;

public class DefaultRedditFetch implements DefaultRedditFetchProvider {
    private final String api_url;
    private final String addition;

    public DefaultRedditFetch(String api_url, String addition) {
        this.api_url = api_url;
        this.addition = addition;
    }

    @Override
    public String fetchInformation() {
        DefaultHttpService httpService = new DefaultHttpService(api_url, addition);
        return httpService.sendRequest().body();
    }
}
