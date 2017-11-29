package com.semihunaldi.intellij.ideacurrency.plugin.toolwindow.component;

import com.intellij.ui.JBColor;
import com.intellij.ui.RoundedLineBorder;
import org.knowm.xchange.dto.marketdata.Ticker;

import javax.swing.*;
import java.awt.*;

/**
 * Created by semihunaldi on 29/11/2017
 */
public class CurrencyCellRenderer extends DefaultListCellRenderer {
    private JPanel contentPane;
    private JLabel currencyNameField;
    private JLabel askField;
    private JLabel bidField;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Ticker ticker = (Ticker) value;
        setData(ticker);
        contentPane.setBackground(JBColor.LIGHT_GRAY);
        contentPane.setBorder(new RoundedLineBorder(JBColor.BLACK,10,3));
        return contentPane;
    }

    private void setData(Ticker ticker) {
        currencyNameField.setText(ticker.getCurrencyPair().toString());
        askField.setText(ticker.getAsk().toPlainString());
        bidField.setText(ticker.getBid().toPlainString());
        askField.setForeground(JBColor.RED);
        bidField.setForeground(JBColor.GREEN);
    }
}
