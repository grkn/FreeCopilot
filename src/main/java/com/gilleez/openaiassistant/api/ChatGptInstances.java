package com.gilleez.openaiassistant.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ChatGptInstances {

    CHAT_GPT_INSTANCES;

    final static Map<String, Object> instanceMap = new ConcurrentHashMap<>();

    static {
        load(Map.of(ChatGptCodexClient.class.getName(),
                ChatGptCodexClientInstance.CHAT_GPT_CODEX_CLIENT_INSTANCE.getInstance()));
    }

    static void load(Map<String, Object> instances) {
        for (Map.Entry<String, Object> entry : instances.entrySet()) {
            instanceMap.putIfAbsent(entry.getKey(), entry.getValue());
        }
    }

    public <T> T getInstance(Class<T> clazz) {

        if (clazz == null) {
            throw new RuntimeException("instance clazz can not be null");
        }
        return (T) instanceMap.get(clazz.getName());
    }
}
