package com.semihunaldi.intellij.ideacurrency.plugin.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Property;
import com.intellij.util.xmlb.annotations.Transient;
import com.semihunaldi.intellij.ideacurrency.plugin.model.SelectedExchangeCurrencyPair;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.knowm.xchange.currency.CurrencyPair;

import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static com.semihunaldi.intellij.ideacurrency.plugin.ApplicationConstants.RELOAD_INTERVAL_MAX;
import static com.semihunaldi.intellij.ideacurrency.plugin.ApplicationConstants.RELOAD_INTERVAL_MIN;

@State(name = "IdeaCurrencyConfig", storages = @Storage(file = "idea_currency_plugin_settings.xml"))
public class IdeaCurrencyConfig implements PersistentStateComponent<IdeaCurrencyConfig> {

    @Transient
    public Set<SelectedExchangeCurrencyPair> selectedExchangeCurrencyPairs = Sets.newHashSet();

    //in seconds
    @Property
    public Integer reloadInterval = 15;

    @Property
    public Map<String, String> currencyPairByExchangeMap = Maps.newHashMap();

    @Property
    public boolean active = true;

    public IdeaCurrencyConfig() {
    }

    @Nullable
    @Override
    public IdeaCurrencyConfig getState() {
        return this;
    }

    @Override
    public void loadState(IdeaCurrencyConfig ideaCurrencyConfig) {
        XmlSerializerUtil.copyBean(ideaCurrencyConfig, this);
        for (String exchangeName : ideaCurrencyConfig.currencyPairByExchangeMap.keySet()) {
            String currencyPairs = currencyPairByExchangeMap.get(exchangeName);
            String[] split = currencyPairs.split(",");
            Set<CurrencyPair> currencyPairSet = Lists.newArrayList(split).stream().map(CurrencyPair::new).collect(Collectors.toSet());
            selectedExchangeCurrencyPairs.add(new SelectedExchangeCurrencyPair(exchangeName, currencyPairSet));
        }
        checkAndOptimizeReloadInterval(ideaCurrencyConfig.getReloadInterval());
    }

    @Transient
    public Set<SelectedExchangeCurrencyPair> getSelectedExchangeCurrencyPairs() {
        return selectedExchangeCurrencyPairs;
    }

    @Transient
    public void setSelectedExchangeCurrencyPairs(Set<SelectedExchangeCurrencyPair> selectedExchangeCurrencyPairs) {
        this.selectedExchangeCurrencyPairs = selectedExchangeCurrencyPairs;
        for (SelectedExchangeCurrencyPair selectedExchangeCurrencyPair : selectedExchangeCurrencyPairs) {
            StringJoiner stringJoiner = new StringJoiner(",");
            for (CurrencyPair currencyPair : selectedExchangeCurrencyPair.getCurrencyPairList()) {
                stringJoiner.add(currencyPair.toString());
            }
            if (StringUtils.isNotBlank(stringJoiner.toString())) {
                currencyPairByExchangeMap.put(selectedExchangeCurrencyPair.getExchangeName(), stringJoiner.toString());
            }
        }
    }

    private void checkAndOptimizeReloadInterval(Integer value) {
        if (reloadInterval == null) {
            reloadInterval = RELOAD_INTERVAL_MIN;
        }
        if (value < RELOAD_INTERVAL_MIN) {
            reloadInterval = RELOAD_INTERVAL_MIN;
        }
        if (value > RELOAD_INTERVAL_MAX) {
            reloadInterval = RELOAD_INTERVAL_MAX;
        }
    }

    public static IdeaCurrencyConfig getInstance() {
        return ServiceManager.getService(IdeaCurrencyConfig.class);
    }


    public Map<String, String> getCurrencyPairByExchangeMap() {
        return currencyPairByExchangeMap;
    }

    public void setCurrencyPairByExchangeMap(Map<String, String> currencyPairByExchangeMap) {
        this.currencyPairByExchangeMap = currencyPairByExchangeMap;
    }

    public Integer getReloadInterval() {
        return reloadInterval;
    }

    public void setReloadInterval(Integer reloadInterval) {
        this.reloadInterval = reloadInterval;
        checkAndOptimizeReloadInterval(reloadInterval);
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}