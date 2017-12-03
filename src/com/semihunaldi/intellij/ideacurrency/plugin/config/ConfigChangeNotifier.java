package com.semihunaldi.intellij.ideacurrency.plugin.config;

import com.intellij.util.messages.Topic;

/**
 * Created by semihunaldi on 04/12/2017
 */
public interface ConfigChangeNotifier {
    Topic<ConfigChangeNotifier> CONFIG_TOPIC = Topic.create("Idea Currency Config", ConfigChangeNotifier.class);

    void configChanged(boolean active);
}

