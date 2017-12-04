package com.semihunaldi.intellij.ideacurrency.plugin;

import com.intellij.ui.treeStructure.Tree;
import com.semihunaldi.intellij.ideacurrency.plugin.model.TickerDto;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitstamp.BitstampExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.service.marketdata.MarketDataService;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Enumeration;

/**
 * Created by semihunaldi on 29/11/2017
 */
public class Util {

    public static TickerDto getSampleTicker() {
        Exchange bitstamp = ExchangeFactory.INSTANCE.createExchange(BitstampExchange.class.getName());
        MarketDataService marketDataService = bitstamp.getMarketDataService();
        Ticker ticker = null;
        try {
            return new TickerDto(bitstamp.getExchangeSpecification().getExchangeName(), marketDataService.getTicker(CurrencyPair.BTC_USD));
        } catch (IOException e) {
            return new TickerDto(bitstamp.getExchangeSpecification().getExchangeName(), new Ticker.Builder().currencyPair(CurrencyPair.BTC_USD).ask(new BigDecimal("123")).bid(new BigDecimal("4253")).build());
        }
    }

    public static void expandAll(Tree tree, TreePath parent, boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children() ; e.hasMoreElements() ; ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }
}
