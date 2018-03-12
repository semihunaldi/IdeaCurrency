package com.semihunaldi.intellij.ideacurrency.plugin.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.progress.impl.BackgroundableProcessIndicator;
import com.semihunaldi.intellij.ideacurrency.plugin.IdeaCurrencyApp;
import com.semihunaldi.intellij.ideacurrency.plugin.config.IdeaCurrencyConfig;
import com.semihunaldi.intellij.ideacurrency.plugin.model.SelectedExchangeCurrencyPair;
import com.semihunaldi.intellij.ideacurrency.plugin.model.TickerDto;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

import static com.semihunaldi.intellij.ideacurrency.plugin.ApplicationConstants.APP_NAME;

/**
 * Created by semihunaldi on 03/12/2017
 */
public class DataFetchTask implements Runnable {

    private Callback callback;
    private DataUpdated dataUpdated;

    public DataFetchTask(Callback callback, DataUpdated dataUpdated) {
        this.callback = callback;
        this.dataUpdated = dataUpdated;
    }

    @Override
    public void run() {
        Task.Backgroundable backgroundable = new Task.Backgroundable(null, APP_NAME, true) {
            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                fetch();
                callback.scheduleNext();
                progressIndicator.stop();
            }
        };
        ApplicationManager.getApplication().invokeLater(() -> {
            ProgressIndicator progressIndicator = new BackgroundableProcessIndicator(backgroundable);
            progressIndicator.setText(APP_NAME);
            ProgressManager.getInstance().runProcessWithProgressAsynchronously(backgroundable, progressIndicator);
        });
    }

    private void fetch() {
        Set<SelectedExchangeCurrencyPair> selectedExchangeCurrencyPairs = IdeaCurrencyConfig.getInstance().getSelectedExchangeCurrencyPairs();
        List<TickerDto> tickers = IdeaCurrencyApp.getInstance().getTickers(selectedExchangeCurrencyPairs);
        dataUpdated.provideData(tickers);
    }

    public static DataFetchTask create(Callback callback, DataUpdated dataUpdated) {
        return new DataFetchTask(callback, dataUpdated);
    }

    public interface Callback {
        void scheduleNext();
    }

    public interface DataUpdated {
        void provideData(List<TickerDto> tickers);
    }
}
