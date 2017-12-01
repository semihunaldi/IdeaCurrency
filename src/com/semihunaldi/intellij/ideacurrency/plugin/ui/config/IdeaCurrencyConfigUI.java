package com.semihunaldi.intellij.ideacurrency.plugin.ui.config;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.CheckboxTree;
import com.intellij.ui.CheckedTreeNode;
import com.intellij.ui.TreeSpeedSearch;
import com.semihunaldi.intellij.ideacurrency.plugin.ApplicationConstants;
import com.semihunaldi.intellij.ideacurrency.plugin.IdeaCurrencyApp;
import com.semihunaldi.intellij.ideacurrency.plugin.config.IdeaCurrencyConfig;
import com.semihunaldi.intellij.ideacurrency.plugin.model.SelectedExchangeCurrencyPair;
import org.jetbrains.annotations.Nls;
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

    public void init() {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.removeAllChildren();
        for (String availableExchangeName : availableExchangeNames) {
            DefaultMutableTreeNode firstChild = new DefaultMutableTreeNode(availableExchangeName);
            Collection<CurrencyPair> currencyPairs = IdeaCurrencyApp.getInstance().getCurrencyPairs(availableExchangeName);
            for (CurrencyPair currencyPair : currencyPairs) {
                CheckedTreeNode secondChild = new CheckedTreeNode(currencyPair.toString());
                firstChild.add(secondChild);
                tree.expandPath(new TreePath(secondChild));
            }
            tree.expandPath(new TreePath(firstChild));
            root.add(firstChild);
        }
        model.reload();
        tree.treeDidChange();
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
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {

    }

    private void createUIComponents() {
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
        init();
    }
}
