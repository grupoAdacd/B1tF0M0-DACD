package com.b1tf0m0.binance.feeder;

import com.b1tf0m0.binance.api.BinanceApi;
import com.b1tf0m0.binance.database.BinanceEventInserter;
import com.b1tf0m0.common.event.DefaultEventBuilder;
import com.b1tf0m0.common.event.EventFileSaver;
import com.b1tf0m0.common.feeder.Feeder;
import org.json.JSONObject;

public class BinanceFeeder implements Feeder {

    @Override
    public void fetchAndSaveEvent() {
        try {
            BinanceApi binanceApi = new BinanceApi("https://api.binance.com/api/v3/ticker/24hr?symbol=", "BTCUSDT");
            String rawJson = binanceApi.fetchInformation();

            DefaultEventBuilder eventBuilder = new DefaultEventBuilder();
            String eventJsonString = eventBuilder.buildEvent("BinanceFeeder", rawJson);

            if (eventJsonString != null) {
                EventFileSaver fileSaver = new EventFileSaver();
                fileSaver.saveEvent("CryptoPrice", "BinanceFeeder", eventJsonString);

                JSONObject eventJson = new JSONObject(eventJsonString);
                BinanceEventInserter inserter = new BinanceEventInserter();
                inserter.insertEvent(eventJson);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
