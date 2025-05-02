package com.b1tf0m0.common.fetch.DefaultBinanceFetchClasses;

import com.b1tf0m0.common.http.DefaultHttpService;

public class DefaultBinanceFetch implements DefaultBinanceFetchProvider {
    private final String Api_url = "https://api.binance.com/api/v3/uiKlines?symbol=";
    private String Symbol = "BTCUSDT";
    private final String Interval = "&interval=6h";
    private final String Limit = "&limit=1000";
    private final String Addition = Symbol + Interval + Limit;

    @Override
    public String fetchWhenInformation(long startDateTime, long endDateTime) {
        DefaultHttpService httpService = new DefaultHttpService(Api_url, Addition + String.format("&startTime=%d&endTime=%d", startDateTime, endDateTime));
        return httpService.sendRequest().body();
    }

    public String getApi_url() {
        return Api_url;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }
}
