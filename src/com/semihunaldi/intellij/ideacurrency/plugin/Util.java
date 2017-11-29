package com.semihunaldi.intellij.ideacurrency.plugin;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitstamp.BitstampExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.service.marketdata.MarketDataService;

import java.io.IOException;

/**
 * Created by semihunaldi on 29/11/2017
 */
public class Util {

    public static Ticker getTicker() {
        Exchange bitstamp = ExchangeFactory.INSTANCE.createExchange(BitstampExchange.class.getName());
        MarketDataService marketDataService = bitstamp.getMarketDataService();
        Ticker ticker = null;
        try {
            return marketDataService.getTicker(CurrencyPair.BTC_USD);
        } catch (IOException e) {
            return null;
        }
    }
}
