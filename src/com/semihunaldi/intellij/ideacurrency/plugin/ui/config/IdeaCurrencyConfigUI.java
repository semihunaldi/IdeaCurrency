package com.semihunaldi.intellij.ideacurrency.plugin.ui.config;

import com.google.common.collect.Sets;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.CheckboxTree;
import com.intellij.ui.CheckboxTreeAdapter;
import com.intellij.ui.CheckedTreeNode;
import com.intellij.ui.TreeSpeedSearch;
import com.semihunaldi.intellij.ideacurrency.plugin.ApplicationConstants;
import com.semihunaldi.intellij.ideacurrency.plugin.IdeaCurrencyApp;
import com.semihunaldi.intellij.ideacurrency.plugin.config.IdeaCurrencyConfig;
import com.semihunaldi.intellij.ideacurrency.plugin.model.SelectedExchangeCurrencyPair;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.knowm.xchange.currency.CurrencyPair;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.util.Collection;
import java.util.Set;

/**
 * Created by semihunaldi on 01/12/2017
 */
public class IdeaCurrencyConfigUI implements Configurable {
    private JPanel jPanel;
    private CheckboxTree tree;
    private Set<SelectedExchangeCurrencyPair> selectedExchangeCurrencyPairs;
    private Collection<String> availableExchangeNames;

    private boolean isModified = false;

    public void init() {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.removeAllChildren();
        for (String availableExchangeName : availableExchangeNames) {
            DefaultMutableTreeNode firstChild = new DefaultMutableTreeNode(availableExchangeName);
            Collection<CurrencyPair> currencyPairs = IdeaCurrencyApp.getInstance().getCurrencyPairs(availableExchangeName);
            for (CurrencyPair currencyPair : currencyPairs) {
                CheckedTreeNode secondChild = new CheckedTreeNode(currencyPair.toString());
                boolean selected = isSelected(availableExchangeName, currencyPair);
                secondChild.setChecked(selected);
                firstChild.add(secondChild);
                tree.expandPath(new TreePath(secondChild));
            }
            tree.expandPath(new TreePath(firstChild));
            root.add(firstChild);
        }
        model.reload();
        tree.treeDidChange();
    }

    private boolean isSelected(String exchangeName, CurrencyPair currencyPair) {
        SelectedExchangeCurrencyPair exchangePair = getExchangePair(exchangeName);
        if(exchangePair != null) {
            return exchangePair.getCurrencyPairList().contains(currencyPair);
        }
        return false;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return ApplicationConstants.APP_NAME;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return jPanel;
    }

    @Override
    public boolean isModified() {
        return isModified;
    }

    @Override
    public void apply() throws ConfigurationException {
        Set<SelectedExchangeCurrencyPair> selectedExchangeCurrencyPairs = Sets.newHashSet();
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        while (root.depthFirstEnumeration().hasMoreElements()) {
            DefaultMutableTreeNode exchangeNameNode = (DefaultMutableTreeNode) root.children().nextElement();
            Set<CurrencyPair> currencyPairs = Sets.newHashSet();
            while (exchangeNameNode.depthFirstEnumeration().hasMoreElements()) { //TODO stucks here
                CheckedTreeNode currencyPairNode = (CheckedTreeNode) exchangeNameNode.children().nextElement();
                if(currencyPairNode.isChecked()) {
                    currencyPairs.add(new CurrencyPair(currencyPairNode.getUserObject().toString()));
                }
            }
            SelectedExchangeCurrencyPair selectedExchangeCurrencyPair = new SelectedExchangeCurrencyPair(exchangeNameNode.getUserObject().toString(), currencyPairs);
            selectedExchangeCurrencyPairs.add(selectedExchangeCurrencyPair);
        }
        IdeaCurrencyConfig.getInstance().setSelectedExchangeCurrencyPairs(selectedExchangeCurrencyPairs);
    }

    private void createUIComponents() {
        isModified = false;
        selectedExchangeCurrencyPairs = IdeaCurrencyConfig.getInstance().selectedExchangeCurrencyPairs;
        availableExchangeNames = IdeaCurrencyApp.getInstance().getAvailableExchangeNames();
        tree = new CheckboxTree(new CheckboxTree.CheckboxTreeCellRenderer() {
            @Override
            public void customizeRenderer(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                final Object o = ((DefaultMutableTreeNode) value).getUserObject();
                if (o != null) {
                    getTextRenderer().append(o.toString());
                }
            }
        }, new CheckedTreeNode());
        tree.setRootVisible(false);
        new TreeSpeedSearch(tree);
        tree.addCheckboxTreeListener(new CheckboxTreeAdapter() {
            @Override
            public void nodeStateChanged(@NotNull CheckedTreeNode node) {
                isModified = true;
            }
        });
        init();
    }

    private SelectedExchangeCurrencyPair getExchangePair(String exchangeName) {
        SelectedExchangeCurrencyPair selectedExchangeCurrencyPair = selectedExchangeCurrencyPairs.stream().filter(secp -> secp.getExchangeName().equals(exchangeName)).findAny().orElse(null);
        return selectedExchangeCurrencyPair;
    }
}
