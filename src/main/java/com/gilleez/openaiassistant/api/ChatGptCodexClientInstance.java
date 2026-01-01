package com.gilleez.openaiassistant.api;

enum ChatGptCodexClientInstance {

    CHAT_GPT_CODEX_CLIENT_INSTANCE(new ChatGptCodexClient());

    private final ChatGptCodexClient instance;

    ChatGptCodexClientInstance(ChatGptCodexClient instance) {
        this.instance = instance;
    }

    public ChatGptCodexClient getInstance() {
        return instance;
    }
}
