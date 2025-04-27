package com.b1tf0m0;

import com.b1tf0m0.common.database.DatabaseManager;
import com.b1tf0m0.common.database.EventLoader;
import com.b1tf0m0.common.scheduler.DefaultFeederScheduler;
import com.b1tf0m0.reddit.database.RedditEventInserter;
import com.b1tf0m0.reddit.feeder.RedditFeeder;

import java.io.File;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();

        RedditFeeder redditFeederCrypto = new RedditFeeder("Cryptocurrency");
        RedditFeeder redditFeederBitcoin = new RedditFeeder("Bitcoin");
        RedditFeeder redditFeederBTC = new RedditFeeder("btc");
        RedditFeeder redditFeederBitcoinNews = new RedditFeeder("BitcoinNews");

        DefaultFeederScheduler scheduler = new DefaultFeederScheduler(Arrays.asList(
                redditFeederCrypto,
                redditFeederBitcoin,
                redditFeederBTC,
                redditFeederBitcoinNews
        ));
        scheduler.start(10);
        System.out.println("RedditFeeders arrancados y capturando eventos...");

        File folder = new File("src/main/eventstore/RedditPost");
        if (folder.exists() && folder.isDirectory()) {
            File[] eventFiles = folder.listFiles((dir, name) -> name.endsWith(".events"));
            if (eventFiles != null) {
                for (File eventFile : eventFiles) {
                    EventLoader.loadEventsFromFile(eventFile, new RedditEventInserter());
                }
            }
        } else {
            System.out.println("No se encontraron archivos de eventos de Reddit para cargar.");
        }
    }
}
