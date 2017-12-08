package com.semihunaldi.intellij.ideacurrency.plugin.config;

import com.intellij.util.messages.Topic;

import static com.semihunaldi.intellij.ideacurrency.plugin.ApplicationConstants.CONFIG_TOPIC_DISPLAY_NAME;

/**
 * Created by semihunaldi on 04/12/2017
 */
public interface ConfigChangeNotifier {
    Topic<ConfigChangeNotifier> CONFIG_TOPIC = Topic.create(CONFIG_TOPIC_DISPLAY_NAME, ConfigChangeNotifier.class);

    void configChanged(boolean active);
}

