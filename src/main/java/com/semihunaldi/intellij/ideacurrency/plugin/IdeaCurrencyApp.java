package com.semihunaldi.intellij.ideacurrency.plugin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intellij.openapi.diagnostic.Logger;
import com.semihunaldi.intellij.ideacurrency.plugin.model.SelectedExchangeCurrencyPair;
import com.semihunaldi.intellij.ideacurrency.plugin.model.TickerDto;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.service.marketdata.MarketDataService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.semihunaldi.intellij.ideacurrency.plugin.ApplicationConstants.DEFAULT_CURRENCY_PAIRS;

/**
 * Created by semihunaldi on 01/12/2017
 */
public class IdeaCurrencyApp {

    private final Logger LOG = Logger.getInstance(getClass());

    private static IdeaCurrencyApp ourInstance;

    public static IdeaCurrencyApp getInstance() {
        if (ourInstance != null) {
            return ourInstance;
        }
        ourInstance = new IdeaCurrencyApp();
        return ourInstance;
    }

    private Map<Class<? extends Exchange>, Exchange> exchanges;

    private IdeaCurrencyApp() {
        exchanges = Maps.newHashMap();
        prepareAvailableExchanges();
    }

    private void prepareAvailableExchanges() {
        for (Class<? extends Exchange> exchangeClass : ApplicationConstants.exchangeClasses) {
            try{
                Exchange exchange = ExchangeFactory.INSTANCE.createExchange(exchangeClass.getName());
                exchanges.put(exchangeClass, exchange);
            } catch(Exception e){
                LOG.info(e);
            }
        }
    }

    public Collection<Exchange> getAvailableExchanges() {
        return exchanges.values();
    }

    public Collection<String> getAvailableExchangeNames() {
        return exchanges.values().stream().map(exchange -> exchange.getExchangeSpecification().getExchangeName()).collect(Collectors.toList());
    }

    public Collection<CurrencyPair> getCurrencyPairs(String exchangeName) {
        Exchange exchangeByName = getExchangeByName(exchangeName);
        if (exchangeByName.getExchangeMetaData() != null) {
            return exchangeByName.getExchangeMetaData().getCurrencyPairs().keySet();
        } else {
            return DEFAULT_CURRENCY_PAIRS;
        }
    }

    public Exchange getExchangeByName(String exchangeName) {
        return exchanges.values().stream().filter(exchange -> exchange.getExchangeSpecification().getExchangeName().equals(exchangeName)).findFirst().orElse(null);
    }

    public Exchange getExchangeByClass(Class<? extends Exchange> exchangeClass) {
        return exchanges.get(exchangeClass);
    }

    public TickerDto getTicker(String exchangeName, CurrencyPair pair) {
        Exchange exchange = getExchangeByName(exchangeName);
        try {
            MarketDataService marketDataService = exchange.getMarketDataService();
            return new TickerDto(exchange.getExchangeSpecification().getExchangeName(), marketDataService.getTicker(pair));
        } catch (Exception e) {
            return new TickerDto(true, exchangeName, pair);
        }
    }

    public List<TickerDto> getTickers(Collection<SelectedExchangeCurrencyPair> selectedExchangeCurrencyPairs) {
        List<TickerDto> tickerDtoList = Lists.newArrayList();
        for (SelectedExchangeCurrencyPair selectedExchangeCurrencyPair : selectedExchangeCurrencyPairs) {
            for (CurrencyPair currencyPair : selectedExchangeCurrencyPair.getCurrencyPairList()) {
                TickerDto ticker = getTicker(selectedExchangeCurrencyPair.getExchangeName(), currencyPair);
                tickerDtoList.add(ticker);
            }
        }
        return tickerDtoList;
    }
}
