package com.b1tf0m0.common.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DATABASE_URL = "jdbc:sqlite:src/main/database/events.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {

            String sqlReddit = "CREATE TABLE IF NOT EXISTS RedditEvents (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "subreddit TEXT NOT NULL," +
                    "author TEXT NOT NULL," +
                    "title TEXT NOT NULL," +
                    "selftext TEXT," +
                    "linkToSite TEXT," +
                    "numComments INTEGER," +
                    "subscribers INTEGER," +
                    "sentiment TEXT," +
                    "createdAt TEXT NOT NULL" +
                    ");";

            String sqlBinance = "CREATE TABLE IF NOT EXISTS BinanceEvents (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "volume REAL," +
                    "openPrice REAL," +
                    "lowPrice REAL," +
                    "highPrice REAL," +
                    "priceChangePercent REAL," +
                    "currentPrice REAL," +
                    "date TEXT NOT NULL" +
                    ");";

            stmt.execute(sqlReddit);
            stmt.execute(sqlBinance);

            System.out.println("Base de datos y tablas inicializadas correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getDatabaseUrl() {
        return DATABASE_URL;
    }

    public static void main(String[] args) {
        initializeDatabase();
    }
}
