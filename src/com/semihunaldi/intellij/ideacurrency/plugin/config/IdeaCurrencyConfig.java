package com.semihunaldi.intellij.ideacurrency.plugin.config;

import com.google.common.collect.Sets;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.semihunaldi.intellij.ideacurrency.plugin.IdeaCurrencyApp;
import com.semihunaldi.intellij.ideacurrency.plugin.model.SelectedExchangeCurrencyPair;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@State(name = "IdeaCurrencySettings", storages = @Storage("idea_currency_plugin_settings.xml"))
public class IdeaCurrencyConfig implements PersistentStateComponent<IdeaCurrencyConfig> {

    public Set<SelectedExchangeCurrencyPair> selectedExchangeCurrencyPairs = Sets.newHashSet();

    @Nullable
    @Override
    public IdeaCurrencyConfig getState() {
        return this;
    }

    @Override
    public void loadState(IdeaCurrencyConfig ideaCurrencyConfig) {
        XmlSerializerUtil.copyBean(ideaCurrencyConfig, this);
    }

    public Set<SelectedExchangeCurrencyPair> getSelectedExchangeCurrencyPairs() {
        return selectedExchangeCurrencyPairs;
    }

    public void setSelectedExchangeCurrencyPairs(Set<SelectedExchangeCurrencyPair> selectedExchangeCurrencyPairs) {
        this.selectedExchangeCurrencyPairs = selectedExchangeCurrencyPairs;
    }


    public static IdeaCurrencyConfig getInstance() {
        return ServiceManager.getService(IdeaCurrencyConfig.class);
    }
}