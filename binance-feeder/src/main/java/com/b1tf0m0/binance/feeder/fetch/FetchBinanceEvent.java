package com.b1tf0m0.binance.feeder.fetch;

import com.b1tf0m0.binance.api.BinanceApi;
import com.b1tf0m0.common.event.DefaultEventBuilder;

public class FetchBinanceEvent {
    private static final String SOURCE_SYSTEM = "BinanceFeeder";

    public String fetchFeederEvent(){
        System.out.println("Ejecutando fetchAndSaveEvent en BinanceFeeder...");
        try {
            BinanceApi binanceApi = new BinanceApi("https://api.binance.com/api/v3/ticker/24hr?symbol=", "BTCUSDT");
            String rawJson = binanceApi.fetchInformation();
            DefaultEventBuilder eventBuilder = new DefaultEventBuilder();
            return eventBuilder.buildEvent(SOURCE_SYSTEM, rawJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
