package com.b1tf0m0;

import com.b1tf0m0.binance.database.BinanceEventInserter;
import com.b1tf0m0.binance.feeder.BinanceFeeder;
import com.b1tf0m0.common.database.DatabaseManager;
import com.b1tf0m0.common.database.EventLoader;
import com.b1tf0m0.common.scheduler.DefaultFeederScheduler;

import java.io.File;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();

        BinanceFeeder binanceFeeder = new BinanceFeeder();
        DefaultFeederScheduler scheduler = new DefaultFeederScheduler(Arrays.asList(
                binanceFeeder
        ));
        scheduler.start(10);
        System.out.println("BinanceFeeder arrancado y capturando eventos...");

        File folder = new File("src/main/eventstore/CryptoPrice/BinanceFeeder");
        if (folder.exists() && folder.isDirectory()) {
            File[] eventFiles = folder.listFiles((dir, name) -> name.endsWith(".events"));
            if (eventFiles != null) {
                for (File eventFile : eventFiles) {
                    EventLoader.loadEventsFromFile(eventFile, new BinanceEventInserter());
                }
            }
        } else {
            System.out.println("No se encontraron archivos de eventos de Binance para cargar.");
        }
    }
}
