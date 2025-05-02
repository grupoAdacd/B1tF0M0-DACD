package com.b1tf0m0.binance.feeder.fetch;

import com.b1tf0m0.binance.api.BinanceApi;
import com.b1tf0m0.binance.database.BinanceEventInserter;
import com.b1tf0m0.common.event.DefaultEventBuilder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;


public class FetchBinanceEvent {
    private final long now = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
    private static final String SOURCE_SYSTEM = "BinanceFeeder";

    public String fetchFeederEvent(){
        System.out.println("Ejecutando fetchAndSaveEvent en BinanceFeeder...");
        try {
            BinanceApi binanceApi = new BinanceApi("BTCUSDT");
            BinanceEventInserter inserter = new BinanceEventInserter();
            String rawJson = binanceApi.fetchWhenInformation(inserter.getLastKlineIntroduced(), now);
            DefaultEventBuilder eventBuilder = new DefaultEventBuilder();
            System.out.println(rawJson);
            return eventBuilder.buildEvent(SOURCE_SYSTEM, rawJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
