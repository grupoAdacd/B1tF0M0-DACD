package com.b1tf0m0.binance.database;

import com.b1tf0m0.common.database.DatabaseManager;
import com.b1tf0m0.common.database.EventInserter;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BinanceEventInserter implements EventInserter {

    // TODO change the date to the real one
    @Override
    public void insertEvent(JSONObject event) {
        try (Connection conn = DriverManager.getConnection(DatabaseManager.getDatabaseUrl())) {
            JSONObject attributes = event.getJSONObject("atributos");

            String sql = "INSERT INTO BinanceEvents (volume, openPrice, lowPrice, highPrice, priceChangePercent, currentPrice, date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDouble(1, attributes.getDouble("volume"));
                pstmt.setDouble(2, attributes.getDouble("openPrice"));
                pstmt.setDouble(3, attributes.getDouble("lowPrice"));
                pstmt.setDouble(4, attributes.getDouble("highPrice"));
                pstmt.setDouble(5, attributes.getDouble("priceChangePercent"));
                pstmt.setDouble(6, attributes.getDouble("lastPrice"));

                String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                pstmt.setString(7, currentDate);

                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
