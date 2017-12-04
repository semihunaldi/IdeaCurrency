package com.semihunaldi.intellij.ideacurrency.plugin.ui.toolwindow;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.JBColor;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.table.JBTable;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import com.semihunaldi.intellij.ideacurrency.plugin.IdeaCurrencyApp;
import com.semihunaldi.intellij.ideacurrency.plugin.config.ConfigChangeNotifier;
import com.semihunaldi.intellij.ideacurrency.plugin.config.IdeaCurrencyConfig;
import com.semihunaldi.intellij.ideacurrency.plugin.model.SelectedExchangeCurrencyPair;
import com.semihunaldi.intellij.ideacurrency.plugin.model.TickerDto;
import com.semihunaldi.intellij.ideacurrency.plugin.service.DataFetchTask;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by semihunaldi on 29/11/2017
 */
public class IdeaCurrencyToolWindow implements ToolWindowFactory {
    private final Logger LOG = Logger.getInstance(getClass());
    private ScheduledExecutorService myExecutor;
    private final List<ScheduledFuture<?>> myScheduledTasks = new LinkedList<>();

    private JPanel contentPane;
    private JButton reloadButton;
    private JBTable table;
    private JLabel lastUpdated;
    private DefaultTableModel defaultTableModel;

    private MessageBusConnection messageBusConnection;

    @Override
    public void init(ToolWindow window) {
        myExecutor = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setDaemon(true).setNameFormat("DataFetch-%s").build());
        table.setDragEnabled(false);
        defaultTableModel = prepareTableHeader();
        reloadButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scheduleTask(IdeaCurrencyConfig.getInstance().getReloadInterval());
            }
        });
        contentPane.setBackground(JBColor.LIGHT_GRAY);
        table.getEmptyText().setText("Please select Currency Pairs to watch from plugin settings");
        scheduleNextTask();
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Set<SelectedExchangeCurrencyPair> selectedExchangeCurrencyPairs = IdeaCurrencyConfig.getInstance().getSelectedExchangeCurrencyPairs();
        if (IdeaCurrencyConfig.getInstance().getActive()) {
            List<TickerDto> data = IdeaCurrencyApp.getInstance().getTickers(selectedExchangeCurrencyPairs);
            fillData(data);
        }
        Content content = contentFactory.createContent(contentPane, "", false);
        toolWindow.getContentManager().addContent(content);

        MessageBus messageBus = project.getMessageBus();
        messageBusConnection = messageBus.connect();
        messageBusConnection.subscribe(ConfigChangeNotifier.CONFIG_TOPIC, active -> {
            if (active) {
                scheduleNextTask();
            }
        });
    }

    private DefaultTableModel prepareTableHeader() {
        DefaultTableModel defaultTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        defaultTableModel.setColumnIdentifiers(new Object[]{"Market", "Bid", "Ask", "Currency"});
        table.setModel(defaultTableModel);
        return defaultTableModel;
    }

    private void fillData(List<TickerDto> tickers) {
        lastUpdated.setText(new Date().toString());
        defaultTableModel.getDataVector().removeAllElements();
        defaultTableModel.fireTableDataChanged();
        for (TickerDto tickerDto : tickers) {
            List<String> columns = Lists.newArrayList();
            if (tickerDto.isFailed()) {
                columns.add(tickerDto.getExchangeName());
                columns.add("-");
                columns.add("-");
                columns.add(tickerDto.getPair().toString());
            } else {
                columns.add(tickerDto.getExchangeName());
                columns.add(tickerDto.getTicker().getBid().setScale(6, BigDecimal.ROUND_DOWN).toPlainString());
                columns.add(tickerDto.getTicker().getAsk().setScale(6, BigDecimal.ROUND_DOWN).toPlainString());
                columns.add(tickerDto.getTicker().getCurrencyPair().toString());
            }
            defaultTableModel.addRow(columns.toArray());
        }
    }

    private void createUIComponents() {
        table = new JBTable() {
            @NotNull
            @Override
            public Component prepareRenderer(@NotNull TableCellRenderer renderer, int row, int col) {
                Component comp = super.prepareRenderer(renderer, row, col);
                if (col == 1) {
                    comp.setForeground(JBColor.BLACK);
                    comp.setBackground(JBColor.GREEN);
                } else if (col == 2) {
                    comp.setForeground(JBColor.BLACK);
                    comp.setBackground(JBColor.RED);
                } else {
                    comp.setForeground(JBColor.BLACK);
                    comp.setBackground(JBColor.WHITE);
                }
                return comp;
            }
        };
    }

    private void scheduleTask(int delaySeconds) {
        synchronized (this) {
            if (cleanAndCheckTasks()) {
                LOG.debug("Scheduling data fetch in ", delaySeconds, "  seconds");
                myScheduledTasks.add(myExecutor.schedule(DataFetchTask.create(this::scheduleNextTask, this::fillData), delaySeconds, TimeUnit.SECONDS));
            } else {
                LOG.debug("Tasks already scheduled");
            }
        }
    }

    private boolean cleanAndCheckTasks() {
        synchronized (this) {
            myScheduledTasks.removeIf(task -> task.isCancelled() || task.isDone());
            return myScheduledTasks.isEmpty();
        }
    }

    public void scheduleNextTask() {
        if (IdeaCurrencyConfig.getInstance().getActive()) {
            synchronized (this) {
                scheduleTask(IdeaCurrencyConfig.getInstance().getReloadInterval());
            }
        }
    }
}
