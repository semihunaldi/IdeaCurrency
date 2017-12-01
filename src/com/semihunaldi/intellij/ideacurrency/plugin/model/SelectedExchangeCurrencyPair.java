package com.semihunaldi.intellij.ideacurrency.plugin.model;

import org.knowm.xchange.currency.CurrencyPair;

import java.util.Set;

/**
 * Created by semihunaldi on 01/12/2017
 */
public class SelectedExchangeCurrencyPair {
    private String exchangeName;
    private Set<CurrencyPair> currencyPairList;

    public SelectedExchangeCurrencyPair(String exchangeName, Set<CurrencyPair> currencyPairList) {
        this.exchangeName = exchangeName;
        this.currencyPairList = currencyPairList;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public Set<CurrencyPair> getCurrencyPairList() {
        return currencyPairList;
    }

    public void setCurrencyPairList(Set<CurrencyPair> currencyPairList) {
        this.currencyPairList = currencyPairList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SelectedExchangeCurrencyPair that = (SelectedExchangeCurrencyPair) o;

        return exchangeName != null ? exchangeName.equals(that.exchangeName) : that.exchangeName == null;
    }

    @Override
    public int hashCode() {
        return exchangeName != null ? exchangeName.hashCode() : 0;
    }
}
