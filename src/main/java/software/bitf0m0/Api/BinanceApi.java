package software.bitf0m0.Api;

import software.bitf0m0.FetchUtils.DefaultFetch;
import software.bitf0m0.JSONUtils.JSONParse;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class BinanceApi extends DefaultFetch {
    private static final String base_fetch_api_url = "https://api.binance.com/api/v3/ticker/24hr?symbol=";
    private static final String fetch_when_api_url = "https://api.binance.com/api/v3/uiKlines?symbol=";
    private static final String symbol = "BTCUSDT";
    private static LocalDateTime startDateTime;
    private static LocalDateTime endDateTime = LocalDateTime.now();
    private static final String interval = "&interval=6h";
    public static String limit = "&limit=1000";

    public static void main(String[] args) throws IOException {
        System.out.println(obtainFullResponse());
    }

    public static ArrayList<String> obtainFullResponse() throws IOException {
        ArrayList<String> fullResponse = new ArrayList<>();
        BinanceApi binanceApi = new BinanceApi(fetch_when_api_url, symbol.concat(interval + limit));
        for (int i = 0; i < 11; i++) {
            String response = binanceApi.fetchWhenInformation(startDateTime, endDateTime);
            fullResponse.add(response);
            try {
                JSONParse responseJSArray = new JSONParse(response);
                String lastKline = responseJSArray.parseArray().get(999).toString();
                String[] klineElements = lastKline.replace("[", "").replace("]", "").split(",");
                if (klineElements.length >= 7) {
                    long closeTimeMillis = Long.parseLong(klineElements[6].trim());
                    Instant instant = Instant.ofEpochMilli(closeTimeMillis + 1);
                    startDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
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

    public BinanceApi(String api_url, String symbol) {
        super(api_url, symbol);
    }

    @Override
    public String fetchInformation() {
        return super.fetchInformation();
    }

    @Override
    public String fetchWhenInformation(LocalDateTime startTime, LocalDateTime endTime) {
        return super.fetchWhenInformation(startTime, endTime);
    }

}
