package com.b1tf0m0.binance.feeder;

import com.b1tf0m0.binance.feeder.save.SaveBinanceEvent;
import com.b1tf0m0.binance.feeder.fetch.FetchBinanceEvent;
import com.b1tf0m0.common.feeder.Feeder;

public class BinanceFeeder implements Feeder {
    @Override
    public void feederDataFetchAndSave() {
        FetchBinanceEvent fetcher = new FetchBinanceEvent();
        SaveBinanceEvent saver = new SaveBinanceEvent();
        System.out.println(fetcher.fetchFeederEvent());
        saver.saveFeederEvent(fetcher.fetchFeederEvent());
    }
}
