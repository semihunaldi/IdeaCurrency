package com.semihunaldi.intellij.ideacurrency.plugin.model;

import org.knowm.xchange.currency.CurrencyPair;

import java.util.List;

/**
 * Created by semihunaldi on 01/12/2017
 */
public class SelectedExchangeCurrencyPair {
    private String exchangeName;
    private List<CurrencyPair> currencyPairList;

    public SelectedExchangeCurrencyPair(String exchangeName, List<CurrencyPair> currencyPairList) {
        this.exchangeName = exchangeName;
        this.currencyPairList = currencyPairList;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public List<CurrencyPair> getCurrencyPairList() {
        return currencyPairList;
    }

    public void setCurrencyPairList(List<CurrencyPair> currencyPairList) {
        this.currencyPairList = currencyPairList;
    }
}
