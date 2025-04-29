package com.b1tf0m0.binance.feeder.save;

import com.b1tf0m0.binance.database.BinanceEventInserter;
import com.b1tf0m0.binance.processor.BinanceRawProcessor;
import com.b1tf0m0.common.broker.EventPublisher;
import com.b1tf0m0.common.event.EventFileSaver;


public class SaveBinanceEvent {
    private static final String SOURCE_SYSTEM = "BinanceFeeder";
    private static final String TOPIC_NAME = "CryptoPrice";


    public void saveFeederEvent(String eventJsonString) {
        if (eventJsonString != null) {
            EventFileSaver fileSaver = new EventFileSaver();
            fileSaver.saveEvent(TOPIC_NAME, SOURCE_SYSTEM, eventJsonString);
            BinanceEventInserter inserter = new BinanceEventInserter();
            BinanceRawProcessor processor = new BinanceRawProcessor();
            inserter.insertEvent(processor.processRawToObject(eventJsonString));
            EventPublisher.publishEvent(TOPIC_NAME, eventJsonString, SOURCE_SYSTEM);
            System.out.println("✅ Evento de Binance publicado en topic: " + TOPIC_NAME);
        }
    }
}
