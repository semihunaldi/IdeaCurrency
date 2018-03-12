package com.semihunaldi.intellij.ideacurrency.plugin.ui.config;

import com.google.common.collect.Sets;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.ui.CheckboxTree;
import com.intellij.ui.CheckboxTreeAdapter;
import com.intellij.ui.CheckedTreeNode;
import com.intellij.ui.TreeSpeedSearch;
import com.intellij.util.messages.MessageBus;
import com.semihunaldi.intellij.ideacurrency.plugin.ApplicationConstants;
import com.semihunaldi.intellij.ideacurrency.plugin.IdeaCurrencyApp;
import com.semihunaldi.intellij.ideacurrency.plugin.Util;
import com.semihunaldi.intellij.ideacurrency.plugin.config.ConfigChangeNotifier;
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
import java.util.Enumeration;
import java.util.Set;

import static com.semihunaldi.intellij.ideacurrency.plugin.ApplicationConstants.RELOAD_INTERVAL_MAX;
import static com.semihunaldi.intellij.ideacurrency.plugin.ApplicationConstants.RELOAD_INTERVAL_MIN;

/**
 * Created by semihunaldi on 01/12/2017
 */
public class IdeaCurrencyConfigUI implements Configurable {
    private JPanel jPanel;
    private CheckboxTree tree;
    private JSlider reloadIntervalSlider;
    private JCheckBox activeCheckBox;
    private JLabel sliderLabel;
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
        Util.expandAll(tree, new TreePath(root), true);
    }

    private void prepareReloadIntervalSlider() {
        reloadIntervalSlider = new JSlider();
        sliderLabel = new JLabel();
        Integer reloadInterval = IdeaCurrencyConfig.getInstance().getReloadInterval();
        reloadIntervalSlider.setValue(reloadInterval);
        reloadIntervalSlider.setMinimum(RELOAD_INTERVAL_MIN);
        int value = reloadIntervalSlider.getValue();
        if (value < RELOAD_INTERVAL_MIN) {
            reloadIntervalSlider.setValue(RELOAD_INTERVAL_MIN);
        }
        if (value > RELOAD_INTERVAL_MAX) {
            reloadIntervalSlider.setValue(RELOAD_INTERVAL_MAX);
        }
        sliderLabel.setText(String.valueOf(reloadIntervalSlider.getValue()));
        reloadIntervalSlider.addChangeListener(e -> {
            isModified = true;
            sliderLabel.setText(String.valueOf(reloadIntervalSlider.getValue()));
        });
    }

    private void prepareActiveCheckbox() {
        activeCheckBox = new JCheckBox();
        boolean active = IdeaCurrencyConfig.getInstance().getActive();
        activeCheckBox.setSelected(active);
        activeCheckBox.addChangeListener(e -> isModified = true);
    }

    private void triggerConfigChange() {
        DataContext dataContext = DataManager.getInstance().getDataContextFromFocus().getResult();
        Project project = DataKeys.PROJECT.getData(dataContext);
        if (project != null) {
            MessageBus messageBus = project.getMessageBus();
            messageBus.connect();
            ConfigChangeNotifier configChangeNotifier = messageBus.syncPublisher(ConfigChangeNotifier.CONFIG_TOPIC);
            configChangeNotifier.configChanged(activeCheckBox.isSelected());
        }
    }

    private boolean isSelected(String exchangeName, CurrencyPair currencyPair) {
        SelectedExchangeCurrencyPair exchangePair = getExchangePair(exchangeName);
        if (exchangePair != null) {
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
        Enumeration rootEnum = root.children();
        while (rootEnum.hasMoreElements()) {
            DefaultMutableTreeNode rootEnumObject = (DefaultMutableTreeNode) rootEnum.nextElement();
            String exchangeName = rootEnumObject.getUserObject().toString();
            Enumeration childEnum = rootEnumObject.children();
            Set<CurrencyPair> currencyPairs = Sets.newHashSet();
            while (childEnum.hasMoreElements()) {
                CheckedTreeNode childEnumObject = (CheckedTreeNode) childEnum.nextElement();
                if (childEnumObject.isChecked()) {
                    currencyPairs.add(new CurrencyPair(childEnumObject.getUserObject().toString()));
                }
            }
            SelectedExchangeCurrencyPair selectedExchangeCurrencyPair = new SelectedExchangeCurrencyPair(exchangeName, currencyPairs);
            selectedExchangeCurrencyPairs.add(selectedExchangeCurrencyPair);
        }
        IdeaCurrencyConfig.getInstance().setSelectedExchangeCurrencyPairs(selectedExchangeCurrencyPairs);
        IdeaCurrencyConfig.getInstance().setReloadInterval(reloadIntervalSlider.getValue());
        IdeaCurrencyConfig.getInstance().setActive(activeCheckBox.isSelected());
        isModified = false;
        triggerConfigChange();
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
        prepareReloadIntervalSlider();
        prepareActiveCheckbox();
    }

    private SelectedExchangeCurrencyPair getExchangePair(String exchangeName) {
        return selectedExchangeCurrencyPairs.stream().filter(secp -> secp.getExchangeName().equals(exchangeName)).findAny().orElse(null);
    }
}
