package com.b1tf0m0;

import com.b1tf0m0.common.database.DatabaseManager;
import com.b1tf0m0.common.scheduler.DefaultFeederScheduler;
import com.b1tf0m0.reddit.feeder.RedditFeeder;

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
        scheduler.start(1);

        System.out.println("RedditFeeders arrancados y capturando eventos...");
    }
}
