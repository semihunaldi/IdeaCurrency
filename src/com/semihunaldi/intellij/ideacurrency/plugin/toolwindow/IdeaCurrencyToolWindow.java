package com.semihunaldi.intellij.ideacurrency.plugin.toolwindow;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.semihunaldi.intellij.ideacurrency.plugin.Util;
import com.semihunaldi.intellij.ideacurrency.plugin.toolwindow.component.CurrencyCellRenderer;
import org.jetbrains.annotations.NotNull;
import org.knowm.xchange.dto.marketdata.Ticker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Created by semihunaldi on 29/11/2017
 */
public class IdeaCurrencyToolWindow implements ToolWindowFactory {
    private JPanel contentPane;
    private JBList jbList;
    private JButton reloadButton;

    @Override
    public void init(ToolWindow window) {
        reloadButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prepareData();
            }
        });
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        prepareData();
        Content content = contentFactory.createContent(contentPane, "", false);
        toolWindow.getContentManager().addContent(content);
    }

    private void prepareData() {
        Ticker ticker = Util.getTicker();
        List<Ticker> tickers = Lists.newArrayList();
        if(ticker != null) {
            for(int i = 0 ; i<4 ; i++) {
                tickers.add(ticker);
            }
            jbList.setCellRenderer(new CurrencyCellRenderer());
            jbList.setListData(tickers.toArray());
        }
    }
}
