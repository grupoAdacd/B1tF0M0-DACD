package com.b1tf0m0.binance.database;

import com.b1tf0m0.binance.api.BinanceKline;
import com.b1tf0m0.common.database.DatabaseManager;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class BinanceEventInserter {

    public void insertEvent(BinanceKline binanceKline) {
        try (Connection conn = DriverManager.getConnection(DatabaseManager.getDatabaseUrl())) {
            String sql = "INSERT INTO BinanceEvents (Kline_Open_Time, open_price, high_price, low_price, close_price, volume, Kline_Close_Time, quote_asset_volume, number_of_trades) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                Date KlineOpenTime = new Date(binanceKline.getKlineOpenTime());
                Date KlineCloseTime = new Date(binanceKline.getKlineCloseTime());
                pstmt.setDate(1, KlineOpenTime);
                pstmt.setDouble(2, Double.parseDouble(binanceKline.getOpenPrice()));
                pstmt.setDouble(3, Double.parseDouble(binanceKline.getHighPrice()));
                pstmt.setDouble(4, Double.parseDouble(binanceKline.getLowPrice()));
                pstmt.setDouble(5, Double.parseDouble(binanceKline.getClosePrice()));
                pstmt.setDouble(6, Double.parseDouble(binanceKline.getVolume()));
                pstmt.setDate(7, KlineCloseTime);
                pstmt.setDouble(8, Double.parseDouble(binanceKline.getQuoteAssetVolume()));
                pstmt.setInt(9, binanceKline.getNumberOfTrades());
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
