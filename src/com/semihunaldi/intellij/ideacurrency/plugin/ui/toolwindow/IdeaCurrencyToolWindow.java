package com.semihunaldi.intellij.ideacurrency.plugin.ui.toolwindow;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.JBColor;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.table.JBTable;
import com.semihunaldi.intellij.ideacurrency.plugin.IdeaCurrencyApp;
import com.semihunaldi.intellij.ideacurrency.plugin.config.IdeaCurrencyConfig;
import com.semihunaldi.intellij.ideacurrency.plugin.model.SelectedExchangeCurrencyPair;
import com.semihunaldi.intellij.ideacurrency.plugin.model.TickerDto;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Set;

/**
 * Created by semihunaldi on 29/11/2017
 */
@SuppressWarnings("Duplicates")
public class IdeaCurrencyToolWindow implements ToolWindowFactory {
    private JPanel contentPane;
    private JButton reloadButton;
    private JBTable table;

    private DefaultTableModel defaultTableModel;

    @Override
    public void init(ToolWindow window) {
        defaultTableModel = prepareTableHeader();
        reloadButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Set<SelectedExchangeCurrencyPair> selectedExchangeCurrencyPairs = IdeaCurrencyConfig.getInstance().getSelectedExchangeCurrencyPairs();
                List<TickerDto> data = IdeaCurrencyApp.getInstance().getTickers(selectedExchangeCurrencyPairs);
                fillData(data);
            }
        });
        contentPane.setBackground(JBColor.LIGHT_GRAY);
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Set<SelectedExchangeCurrencyPair> selectedExchangeCurrencyPairs = IdeaCurrencyConfig.getInstance().getSelectedExchangeCurrencyPairs();
        List<TickerDto> data = IdeaCurrencyApp.getInstance().getTickers(selectedExchangeCurrencyPairs);
        fillData(data);
        Content content = contentFactory.createContent(contentPane, "", false);
        toolWindow.getContentManager().addContent(content);
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
        for (int i = 0 ; i < table.getRowCount() ; i++) { //TODO not working
            table.remove(i);
        }
        for (TickerDto tickerDto : tickers) {
            List<String> columns = Lists.newArrayList();
            columns.add(tickerDto.getExchangeName());
            columns.add(tickerDto.getTicker().getBid().toPlainString());
            columns.add(tickerDto.getTicker().getAsk().toPlainString());
            columns.add(tickerDto.getTicker().getCurrencyPair().toString());
            defaultTableModel.addRow(columns.toArray());
        }
        table.repaint();
    }

    private void createUIComponents() {
        table = new JBTable() {
            @NotNull
            @Override
            public Component prepareRenderer(@NotNull TableCellRenderer renderer, int row, int col) {
                Component comp = super.prepareRenderer(renderer, row, col);
                if (col == 1) {
                    comp.setForeground(JBColor.GREEN);
                    comp.setBackground(JBColor.BLACK);
                } else if (col == 2) {
                    comp.setForeground(JBColor.RED);
                    comp.setBackground(JBColor.BLACK);
                } else {
                    comp.setForeground(JBColor.BLACK);
                    comp.setBackground(JBColor.WHITE);
                }
                return comp;
            }
        };
    }
}
