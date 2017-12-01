package com.semihunaldi.intellij.ideacurrency.plugin;

import com.semihunaldi.intellij.ideacurrency.plugin.model.TickerDto;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitstamp.BitstampExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.service.marketdata.MarketDataService;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by semihunaldi on 29/11/2017
 */
public class Util {

    public static TickerDto getSampleTicker() {
        Exchange bitstamp = ExchangeFactory.INSTANCE.createExchange(BitstampExchange.class.getName());
        MarketDataService marketDataService = bitstamp.getMarketDataService();
        Ticker ticker = null;
        try {
            return new TickerDto(bitstamp.getExchangeSpecification().getExchangeName(),marketDataService.getTicker(CurrencyPair.BTC_USD));
        } catch (IOException e) {
            //TODO return null
            return new TickerDto(bitstamp.getExchangeSpecification().getExchangeName(),new Ticker.Builder().currencyPair(CurrencyPair.BTC_USD).ask(new BigDecimal("123")).bid(new BigDecimal("4253")).build());
        }
    }
}
