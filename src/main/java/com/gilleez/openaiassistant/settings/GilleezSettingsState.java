package com.gilleez.openaiassistant.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Stores non-sensitive settings in IDE config.
 * API key stored in PasswordSafe.
 */
@State(name = "GilleezOpenAISettings", storages = @Storage("gilleez-openai-assistant.xml"))
@Service(Service.Level.APP)
public final class GilleezSettingsState implements PersistentStateComponent<GilleezSettingsState> {

    public String baseUrl = "https://api.openai.com/v1";
    public String model = "gpt-5.1-codex-max";
    public String openAIProjectId = "";
    public String openAIOrganizationId = "";
    public boolean streamingEnabled = false;

    public String getApiKey() {
        // Modify here to test yourself. Add api key from open ai platform and paste it here
        return "your-api-key-for-openai";
    }

    @Override
    public @Nullable GilleezSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull GilleezSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
