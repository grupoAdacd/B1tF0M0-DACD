package com.b1tf0m0.reddit.fetch;

import com.b1tf0m0.reddit.fetch.inTimeFetcherUtils.PeriodicCollector;
import java.util.ArrayList;

public class RedditInTimeFetcher {

    public static void main(String[] args) {
        ArrayList<String> subreddit = new ArrayList<>();
        subreddit.add("Cryptocurrency");
        subreddit.add("Bitcoin");
        subreddit.add("btc");
        subreddit.add("BitcoinNews");

        int collectionInterval = 15;

        for (String sub : subreddit) {
            String url = String.format("https://www.reddit.com/r/%s/new.json?limit=100&t=all", sub);
            PeriodicCollector collector = new PeriodicCollector(url, "", collectionInterval);
            collector.startPeriodicCollection();
            System.out.println("Iniciada recolección para r/" + sub);
        }

        try {
            System.out.println("Programa de recolección iniciado.");
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            System.out.println("Programa detenido");
        }
    }
}