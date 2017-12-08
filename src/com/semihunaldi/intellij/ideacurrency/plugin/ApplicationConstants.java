package com.semihunaldi.intellij.ideacurrency.plugin;

import com.google.common.collect.Lists;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.bitfinex.v1.BitfinexExchange;
import org.knowm.xchange.bitstamp.BitstampExchange;
import org.knowm.xchange.btcturk.BTCTurkExchange;
import org.knowm.xchange.cexio.CexIOExchange;
import org.knowm.xchange.coinbase.CoinbaseExchange;
import org.knowm.xchange.coinmarketcap.CoinMarketCapExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.koineks.KoineksExchange;
import org.knowm.xchange.koinim.KoinimExchange;
import org.knowm.xchange.liqui.LiquiExchange;
import org.knowm.xchange.paribu.ParibuExchange;

import java.util.List;

import static org.knowm.xchange.currency.CurrencyPair.BCC_USD;
import static org.knowm.xchange.currency.CurrencyPair.DASH_USD;
import static org.knowm.xchange.currency.CurrencyPair.DOGE_USD;
import static org.knowm.xchange.currency.CurrencyPair.IOTA_USD;
import static org.knowm.xchange.currency.CurrencyPair.LTC_USD;
import static org.knowm.xchange.currency.CurrencyPair.XMR_BTC;
import static org.knowm.xchange.currency.CurrencyPair.XRP_USD;

/**
 * Created by semihunaldi on 01/12/2017
 */
public class ApplicationConstants {

    public static final Integer RELOAD_INTERVAL_MIN = 3;
    public static final Integer RELOAD_INTERVAL_MAX = 360;

    public static final String SETTINGS_XML_NAME = "idea_currency_plugin_settings.xml";
    public static final String CONFIG_STATE_NAME = "IdeaCurrencyConfig";
    public static final String CONFIG_TOPIC_DISPLAY_NAME = "Idea Currency Config";

    public static final String APP_NAME = "Idea Currency Plugin";

    public static final List<Class<? extends Exchange>> exchangeClasses = Lists.newArrayList(
            BTCTurkExchange.class, ParibuExchange.class, BitstampExchange.class,
            CoinbaseExchange.class, CoinMarketCapExchange.class, LiquiExchange.class,
            BinanceExchange.class, BitfinexExchange.class, CexIOExchange.class, KoineksExchange.class, KoinimExchange.class);

    public static final List<CurrencyPair> DEFAULT_CURRENCY_PAIRS = Lists.newArrayList(CurrencyPair.BTC_USD, CurrencyPair.ETH_USD, LTC_USD, DOGE_USD, DASH_USD, XRP_USD, IOTA_USD, BCC_USD, XMR_BTC);

    public static final Object[] TABLE_COLUMN_IDENTIFIERS = new Object[]{"Market", "Bid", "Ask", "Currency"};

    public static final String TABLE_EMPTY_TEXT = "Please select Currency Pairs to watch from plugin settings";
}
