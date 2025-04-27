package com.b1tf0m0.binance.save;

import com.b1tf0m0.binance.api.BinanceApi;
import com.b1tf0m0.common.json.JSONParse;
import com.b1tf0m0.common.save.DefaultFileSaver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class BinanceDataSaver extends DefaultFileSaver {
    private static final String DATA_DIRECTORY = "src/main/DataLake/DataLake/bitcoin_data";
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String symbol = "BTCUSDT";
    private static LocalDateTime startDateTime = LocalDateTime.of(2015,1, 1,00,00,00);
    private static LocalDateTime endDateTime = LocalDateTime.now();
    private static final String interval = "&interval=1h";
    public static String limit = "&limit=1000";

    public BinanceDataSaver(String data, String where, LocalDateTime now, LocalDateTime name, String DataLakeDirectory) {
        super(data, where, now, name, DataLakeDirectory);
    }

    public static void insertOnFileBinanceHistoricData(String fetchSpecification, LocalDateTime startDateTime, LocalDateTime endDateTime, String fileName) {
        BinanceApi binanceApi = new BinanceApi(fetchSpecification, symbol.concat(interval));
        JSONParse candle = new JSONParse(binanceApi.fetchWhenInformation(startDateTime, endDateTime));

        if (candle.parseArray() != null && !candle.parseArray().isEmpty()) {
            File dataFile = new File(DATA_DIRECTORY + File.separator + fileName);
            try (FileWriter writer = new FileWriter(dataFile)) {
                writer.write("Bitcoin Price Data (" + symbol + ") from " +
                        startDateTime.format(DISPLAY_FORMAT) + " to " +
                        endDateTime.format(DISPLAY_FORMAT) + "\n");
                writer.write("Interval: " + "1h" + "\n\n");
                LocalDateTime candleOpenTime = LocalDateTime.ofEpochSecond(
                        candle.parseArray().getLong(0) / 1000, 0, ZoneOffset.UTC);
                LocalDateTime candleCloseTime = LocalDateTime.ofEpochSecond(
                        candle.parseArray().getLong(6) / 1000, 0, ZoneOffset.UTC);
                writer.write("Kline open Time: " + candleOpenTime.format(DISPLAY_FORMAT) + "\n");
                writer.write("Open Price: " + candle.parseArray().getString(1) + "\n");
                writer.write("High Price: " + candle.parseArray().getString(2) + "\n");
                writer.write("Low Price: " + candle.parseArray().getString(3) + "\n");
                writer.write("Close Price: " + candle.parseArray().getString(4) + "\n");
                writer.write("Volume: " + candle.parseArray().getString(5) + "\n");
                writer.write("Kline close Time: " + candleCloseTime.format(DISPLAY_FORMAT) + "\n");
                System.out.println("Data saved to file: " + fileName +
                        " (" + candle.parseArray().length() + " data points)");
            } catch (IOException e) {
                System.err.println("Error writing to file " + fileName + ": " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("No data available for period: " +
                    startDateTime.format(DISPLAY_FORMAT) + " to " +
                    endDateTime.format(DISPLAY_FORMAT));
        }
    }


    // Todo modify this class to save the today fetch ......................... ESTA MAL QUE NO USE BINANCE DATA SAVER
    public static void insertOnFileBinanceHistoricData(String fetchSpecification, String fileName) {
        BinanceApi binanceApi = new BinanceApi(fetchSpecification, symbol.concat(interval));
        JSONParse candle = new JSONParse(binanceApi.fetchInformation());

        if (candle.parseArray() != null && !candle.parseArray().isEmpty()) {
            File dataFile = new File(DATA_DIRECTORY + File.separator + fileName);
            try (FileWriter writer = new FileWriter(dataFile)) {
                writer.write("Bitcoin Price Data (" + symbol + ") from today");
                writer.write("Interval: " + "1h" + "\n\n");
                LocalDateTime candleOpenTime = LocalDateTime.ofEpochSecond(
                        candle.parseArray().getLong(0) / 1000, 0, ZoneOffset.UTC);
                LocalDateTime candleCloseTime = LocalDateTime.ofEpochSecond(
                        candle.parseArray().getLong(6) / 1000, 0, ZoneOffset.UTC);
                writer.write("Kline open Time: " + candleOpenTime.format(DISPLAY_FORMAT) + "\n");
                writer.write("Open Price: " + candle.parseArray().getString(1) + "\n");
                writer.write("High Price: " + candle.parseArray().getString(2) + "\n");
                writer.write("Low Price: " + candle.parseArray().getString(3) + "\n");
                writer.write("Close Price: " + candle.parseArray().getString(4) + "\n");
                writer.write("Volume: " + candle.parseArray().getString(5) + "\n");
                writer.write("Kline close Time: " + candleCloseTime.format(DISPLAY_FORMAT) + "\n");
                System.out.println("Data saved to file: " + fileName +
                        " (" + candle.parseArray().length() + " data points)");
            } catch (IOException e) {
                System.err.println("Error writing to file " + fileName + ": " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("No data available for period: " +
                    startDateTime.format(DISPLAY_FORMAT) + " to " +
                    endDateTime.format(DISPLAY_FORMAT));
        }
    }
}

