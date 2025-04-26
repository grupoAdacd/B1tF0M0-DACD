package software.bitf0m0.FetchUtils;

import software.bitf0m0.Api.ApiUtils.RedditDataSaver;
import software.bitf0m0.Api.RedditApi;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class RedditInTimeFetcher extends RedditApi {
    private Timer timer;
    private int intervalMinutes;
    private String subredditName;

    public RedditInTimeFetcher(String api_url, String addition, int intervalMinutes) {
        super(api_url, addition);
        this.intervalMinutes = intervalMinutes;
        this.timer = new Timer();

        String url = api_url;
        if (url.contains("/r/")) {
            String[] parts = url.split("/r/");
            if (parts.length > 1) {
                String subPart = parts[1];
                this.subredditName = subPart.split("/")[0];
                if (this.subredditName.contains("?")) {
                    this.subredditName = this.subredditName.split("\\?")[0];
                }
            }
        }
    }

    @Override
    public String fetchInformation() {
        String result = super.fetchInformation();
        System.out.println("Datos obtenidos de r/" + subredditName + " a las " + new java.util.Date());

        RedditDataSaver.insertOnFileRedditData(result, subredditName);

        return result;
    }

    public void startPeriodicCollection() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                fetchInformation();
            }
        }, 0, intervalMinutes * 60 * 1000);
    }

    public void stopCollection() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public static void main(String[] args) {
        ArrayList<String> subreddit = new ArrayList<>();
        subreddit.add("Cryptocurrency");
        subreddit.add("Bitcoin");
        subreddit.add("btc");
        subreddit.add("BitcoinNews");

        int collectionInterval = 15;

        for (String sub : subreddit) {
            String url = String.format("https://www.reddit.com/r/%s/new.json?limit=100&t=all", sub);
            RedditInTimeFetcher collector = new RedditInTimeFetcher(url, "", collectionInterval);
            collector.startPeriodicCollection();
            System.out.println("Iniciada recolección para r/" + sub);
        }

        try {
            System.out.println("Programa de recolección iniciado. Presiona Ctrl+C para detener.");
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            System.out.println("Programa detenido");
        }
    }
}