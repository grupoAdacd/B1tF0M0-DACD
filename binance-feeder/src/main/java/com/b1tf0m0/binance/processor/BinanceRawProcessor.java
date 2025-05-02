package com.b1tf0m0.binance.processor;

import com.b1tf0m0.binance.api.BinanceKline;
import com.b1tf0m0.binance.feeder.BinanceFeeder;
import com.b1tf0m0.common.json.JSONParse;
import org.json.JSONArray;

public class BinanceRawProcessor {

    public BinanceKline processRawToObject(String eventJsonString) {
        if (!eventJsonString.isEmpty() && eventJsonString != null) {
            JSONParse eventParser = new JSONParse(eventJsonString);
            System.out.println(eventParser);
            for (int i=0; i<1000; i++) {
                JSONArray jsonArray = eventParser.parseArray();
                JSONArray Kline = new JSONArray(jsonArray.get(i));
                return new BinanceKline(
                        Kline.getLong(1),
                        Kline.getString(2),
                        Kline.getString(3),
                        Kline.getString(4),
                        Kline.getString(5),
                        Kline.getString(6),
                        Kline.getLong(7),
                        Kline.getString(8),
                        Kline.getInt(9)
                );
            }
        }
        return null;
    }
}
