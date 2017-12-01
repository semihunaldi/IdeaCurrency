package com.semihunaldi.intellij.ideacurrency.plugin;

import com.google.common.collect.Lists;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.bitstamp.BitstampExchange;
import org.knowm.xchange.btcturk.BTCTurkExchange;
import org.knowm.xchange.coinbase.CoinbaseExchange;
import org.knowm.xchange.paribu.ParibuExchange;

import java.util.List;

/**
 * Created by semihunaldi on 01/12/2017
 */
public class ApplicationConstants {
    public static final String APP_NAME = "Idea Currency Plugin";
    public static final String APP_ERROR_ID = "IdeaCurrencyError";

    public static final List<Class<? extends Exchange>> exchangeClasses = Lists.newArrayList(
            BTCTurkExchange.class, ParibuExchange.class, BitstampExchange.class, CoinbaseExchange.class
    );
}
