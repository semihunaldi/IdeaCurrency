package com.semihunaldi.intellij.ideacurrency.plugin.model;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;

/**
 * Created by semihunaldi on 29/11/2017
 */
public class TickerDto {
    private String exchangeName;
    private Ticker ticker;
    private CurrencyPair pair;
    private boolean failed = false;

    public TickerDto(String exchangeName, Ticker ticker) {
        this.exchangeName = exchangeName;
        this.ticker = ticker;
    }

    public TickerDto(boolean failed, String exchangeName, CurrencyPair pair) {
        this.failed = failed;
        this.exchangeName = exchangeName;
        this.pair = pair;
    }

    public TickerDto(String exchangeName, Ticker ticker, boolean failed) {
        this.exchangeName = exchangeName;
        this.ticker = ticker;
        this.failed = failed;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public CurrencyPair getPair() {
        return pair;
    }
}
