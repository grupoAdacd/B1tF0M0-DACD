package com.b1tf0m0.binance.api;

import com.b1tf0m0.binance.processor.BinanceRawProcessor;
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

    public ArrayList<ArrayList<BinanceKline>> obtainFullResponse() {
        ArrayList<ArrayList<BinanceKline>> fullResponse = new ArrayList<>();
        BinanceApi binanceApi = new BinanceApi("BTCUSDT");
        int allTime = 11;
        for (int i=0;i<allTime;i++) {
            String eachResponse = binanceApi.fetchWhenInformation(startDateTime, endDateTime);
            if (eachResponse == null || eachResponse.isEmpty()) {
                break;
            }
            try {
                JSONParse jsonArrayOfKlines = new JSONParse(eachResponse);
                if (jsonArrayOfKlines.parseArray().isEmpty()) {
                    break;
                }
                BinanceRawProcessor processor = new BinanceRawProcessor();
                ArrayList<BinanceKline> binanceKlineArray = processor.processRawToObject(eachResponse);
                if (binanceKlineArray != null && !binanceKlineArray.isEmpty()) {
                    fullResponse.add(binanceKlineArray);
                    BinanceKline lastKline = binanceKlineArray.getLast();
                    setStartDateTime(lastKline.getKlineCloseTime());
                }
            } catch (Exception e) {
                System.err.println("Error processing response: " + e.getMessage());
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
