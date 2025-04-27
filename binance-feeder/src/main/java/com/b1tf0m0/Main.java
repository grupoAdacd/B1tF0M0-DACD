package com.b1tf0m0;

import com.b1tf0m0.binance.feeder.BinanceFeeder;
import com.b1tf0m0.common.scheduler.DefaultFeederScheduler;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        BinanceFeeder binanceFeeder = new BinanceFeeder();

        DefaultFeederScheduler scheduler = new DefaultFeederScheduler(Arrays.asList(
                binanceFeeder
        ));

        scheduler.start(10);
        System.out.println("Schedulers arrancados. Capturando eventos periódicamente...");
    }
}
