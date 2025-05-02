package com.b1tf0m0.binance.processor;

import com.b1tf0m0.binance.api.BinanceKline;
import com.b1tf0m0.common.json.JSONParse;

import java.util.ArrayList;

public class BinanceRawProcessor {
    public ArrayList<BinanceKline> processRawToObject(String eventJsonString) {
        if (!eventJsonString.isEmpty()) {
            try {
                JSONParse eventParser = new JSONParse(eventJsonString);
                ArrayList<BinanceKline> allKlinesObject = new ArrayList<>();
                for (int i = 0; i < eventParser.parseArray().length(); i++) {
                    JSONParse Kline = new JSONParse(eventParser.parseArray().getJSONArray(i).toString());
                    if (Kline.parseArray().length() >= 10){
                        BinanceKline binanceKline = new BinanceKline(
                                Kline.parseArray().getLong(0),
                                Kline.parseArray().getString(1),
                                Kline.parseArray().getString(2),
                                Kline.parseArray().getString(3),
                                Kline.parseArray().getString(4),
                                Kline.parseArray().getString(5),
                                Kline.parseArray().getLong(6),
                                Kline.parseArray().getString(7),
                                Kline.parseArray().getInt(8)
                        );
                        allKlinesObject.add(binanceKline);
                    }
                }
                return allKlinesObject;
            } catch (Exception e){
                System.err.println("Error parsing JSON: "+e.getMessage());
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }
}
