package com.b1tf0m0.common.fetch;

import com.b1tf0m0.common.http.DefaultHttpService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class  DefaultFetch implements DefaultFetchProvider {
    private String api_url;
    private String addition;
    public DefaultFetch(String api_url, String addition) {
        this.api_url = api_url;
        this.addition = addition;
    }

    @Override
    public String fetchInformation() {
        DefaultHttpService httpService = new DefaultHttpService(api_url, addition);
        return httpService.sendRequest().body();
    }

    @Override
    public String fetchWhenInformation(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        long startTimeMillis = startDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        long endTimeMillis = endDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        DefaultHttpService httpService = new DefaultHttpService(api_url, addition + String.format("&startTime=%d&endTime=%d", startTimeMillis, endTimeMillis));
        return httpService.sendRequest().body();
    }
}
