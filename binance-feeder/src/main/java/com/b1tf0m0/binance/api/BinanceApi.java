package com.b1tf0m0.binance.api;

import com.b1tf0m0.binance.feeder.BinanceFeeder;
import com.b1tf0m0.common.fetch.DefaultBinanceFetchClasses.DefaultBinanceFetch;
import com.b1tf0m0.common.json.JSONParse;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class BinanceApi extends DefaultBinanceFetch {
    private long startDateTime;
    private long endDateTime = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();

    public static void main(String[] args) {
        BinanceApi binanceApi = new BinanceApi("BTCUSDT");
        System.out.println(binanceApi.obtainFullResponse());
    }

    public BinanceApi(String symbol) {
        this.setSymbol(symbol);
    }

    public ArrayList<String> obtainFullResponse() {
        ArrayList<String> fullResponse = new ArrayList<>();
        BinanceApi binanceApi = new BinanceApi("BTCUSDT");
        for (int i = 0; i < 11; i++) {
            String response = binanceApi.fetchWhenInformation(startDateTime, endDateTime);
            fullResponse.add(response);
            try {
                JSONParse responseJSArray = new JSONParse(response);
                String lastKline = responseJSArray.parseArray().get(999).toString();
                String[] klineElements = lastKline.replace("[", "").replace("]", "").split(",");
                if (klineElements.length >= 7) {
                    startDateTime = Long.parseLong(klineElements[6].trim());
                } else {
                    System.err.println("Error: formato de kline inesperado");
                    break;
                }
            } catch (Exception e) {
                System.err.println("Error procesando la respuesta: " + e.getMessage());
                break;
            }
        }
        return fullResponse;
    }

    @Override
    public String fetchWhenInformation(long startTime, long endTime) {
        return super.fetchWhenInformation(startTime, endTime);
    }

    public long getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(long startDateTime) {
        this.startDateTime = startDateTime;
    }

    public long getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(long endDateTime) {
        this.endDateTime = endDateTime;
    }
}
