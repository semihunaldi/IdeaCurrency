package com.semihunaldi.intellij.ideacurrency.plugin.model;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;

import java.util.List;

/**
 * Created by semihunaldi on 01/12/2017
 */
public class ExchangeForCurrency {
    private Class<? extends Exchange> exchangeClass;
    private List<CurrencyPair> currencyPairList;
    private CurrencyPair selectedCurrencyPair;

    public ExchangeForCurrency(Class<? extends Exchange> exchangeClass, List<CurrencyPair> currencyPairList) {
        this.exchangeClass = exchangeClass;
        this.currencyPairList = currencyPairList;
    }

    public Class getExchangeClass() {
        return exchangeClass;
    }

    public void setExchangeClass(Class<? extends Exchange> exchangeClass) {
        this.exchangeClass = exchangeClass;
    }

    public List<CurrencyPair> getCurrencyPairList() {
        return currencyPairList;
    }

    public void setCurrencyPairList(List<CurrencyPair> currencyPairList) {
        this.currencyPairList = currencyPairList;
    }

    public CurrencyPair getSelectedCurrencyPair() {
        return selectedCurrencyPair;
    }

    public void setSelectedCurrencyPair(CurrencyPair selectedCurrencyPair) {
        this.selectedCurrencyPair = selectedCurrencyPair;
    }
}
