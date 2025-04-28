package com.b1tf0m0.binance.feeder;

import com.b1tf0m0.binance.api.BinanceApi;
import com.b1tf0m0.binance.database.BinanceEventInserter;
import com.b1tf0m0.common.broker.EventPublisher;
import com.b1tf0m0.common.event.DefaultEventBuilder;
import com.b1tf0m0.common.event.EventFileSaver;
import com.b1tf0m0.common.feeder.Feeder;
import org.json.JSONObject;

public class BinanceFeeder implements Feeder {

    private static final String SOURCE_SYSTEM = "BinanceFeeder";
    private static final String TOPIC_NAME = "CryptoPrice";

    @Override
    public void fetchAndSaveEvent() {
        System.out.println("🔥 Ejecutando fetchAndSaveEvent en BinanceFeeder...");

        try {
            BinanceApi binanceApi = new BinanceApi("https://api.binance.com/api/v3/ticker/24hr?symbol=", "BTCUSDT");
            String rawJson = binanceApi.fetchInformation();

            DefaultEventBuilder eventBuilder = new DefaultEventBuilder();
            String eventJsonString = eventBuilder.buildEvent(SOURCE_SYSTEM, rawJson);

            if (eventJsonString != null) {
                EventFileSaver fileSaver = new EventFileSaver();
                fileSaver.saveEvent(TOPIC_NAME, SOURCE_SYSTEM, eventJsonString);

                JSONObject eventJson = new JSONObject(eventJsonString);
                BinanceEventInserter inserter = new BinanceEventInserter();
                inserter.insertEvent(eventJson);

                EventPublisher.publishEvent(TOPIC_NAME, eventJson, SOURCE_SYSTEM);
                System.out.println("✅ Evento de Binance publicado en topic: " + TOPIC_NAME);
            }
        } catch (Exception e) {
            System.err.println("❌ Error en BinanceFeeder: " + e.getMessage());
            e.printStackTrace();
        }
    }
}