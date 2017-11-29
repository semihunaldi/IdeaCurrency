package com.semihunaldi.intellij.ideacurrency.plugin;

import org.knowm.xchange.dto.marketdata.Ticker;

/**
 * Created by semihunaldi on 29/11/2017
 */
public class TickerDto {
    private String exchangeName;
    private Ticker ticker;

    public TickerDto(String exchangeName, Ticker ticker) {
        this.exchangeName = exchangeName;
        this.ticker = ticker;
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
}
