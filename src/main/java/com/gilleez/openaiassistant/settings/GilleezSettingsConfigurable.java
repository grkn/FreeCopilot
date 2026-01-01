package com.gilleez.openaiassistant.settings;

import com.gilleez.openaiassistant.api.ChatGptCodexClient;
import com.gilleez.openaiassistant.api.ChatGptInstances;
import com.gilleez.openaiassistant.core.ModelResponse;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBTextField;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class GilleezSettingsConfigurable implements Configurable {

    private final GilleezSettingsState state = ServiceManager.getService(GilleezSettingsState.class);

    private JPanel panel;
    private JBTextField baseUrlField;
    private JBTextField modelField;
    private JBTextField projectField;
    private JBTextField orgField;
    private JBPasswordField apiKeyField;
    private JBCheckBox streamingBox;
    private JButton testButton;

    @Override
    public @Nls String getDisplayName() {
        return "Gilleez OpenAI Assistant";
    }

    @Override
    public @Nullable JComponent createComponent() {
        panel = new JPanel(new GridBagLayout());
        baseUrlField = new JBTextField();
        modelField = new JBTextField();
        projectField = new JBTextField();
        orgField = new JBTextField();
        apiKeyField = new JBPasswordField();
        streamingBox = new JBCheckBox("Enable streaming (SSE)");
        testButton = new JButton("Test Connection (GET /models)");

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(4, 4, 4, 4);

        addRow(c, "Base URL", baseUrlField);
        addRow(c, "Model", modelField);
        addRow(c, "OpenAI-Project (optional)", projectField);
        addRow(c, "OpenAI-Organization (optional)", orgField);
        addRow(c, "API Key", apiKeyField);
        addRow(c, " ", streamingBox);

        c.gridx = 1;
        panel.add(testButton, c);

        testButton.addActionListener(e -> {
            apply();
            try {
                ChatGptCodexClient client = ChatGptInstances.CHAT_GPT_INSTANCES.getInstance(ChatGptCodexClient.class);
                ModelResponse models = client.fetchAvailableModelsForCodex();


                String msg = models != null && CollectionUtils.isNotEmpty(models.getData())
                        ? "OK. Model '" + state.model + "' is available."
                        : "Connected. But model '" + state.model + "' not found in /models list. (gilleez: check project/org headers)";
                Messages.showInfoMessage(msg, "Gilleez OpenAI Assistant");
            } catch (Exception ex) {
                Messages.showErrorDialog(
                        "Failed: " + ex.getMessage() + "\n\n(gilleez: check API key, project/org headers, and billing/quota)",
                        "Gilleez OpenAI Assistant"
                );
            }
        });

        reset();
        return panel;
    }

    private void addRow(GridBagConstraints c, String label, JComponent comp) {
        c.gridx = 0;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(label), c);

        c.gridx = 1;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(comp, c);
        c.gridy++;
    }

    @Override
    public boolean isModified() {
        return !baseUrlField.getText().trim().equals(state.baseUrl)
                || !modelField.getText().trim().equals(state.model)
                || !projectField.getText().trim().equals(state.openAIProjectId)
                || !orgField.getText().trim().equals(state.openAIOrganizationId)
                || !String.valueOf(apiKeyField.getPassword()).equals(state.getApiKey())
                || streamingBox.isSelected() != state.streamingEnabled;
    }

    @Override
    public void apply() {
        state.baseUrl = baseUrlField.getText().trim();
        state.model = modelField.getText().trim();
        state.openAIProjectId = projectField.getText().trim();
        state.openAIOrganizationId = orgField.getText().trim();
        state.streamingEnabled = streamingBox.isSelected();
    }

    @Override
    public void reset() {
        baseUrlField.setText(state.baseUrl);
        modelField.setText(state.model);
        projectField.setText(state.openAIProjectId);
        orgField.setText(state.openAIOrganizationId);
        apiKeyField.setText(state.getApiKey());
        streamingBox.setSelected(state.streamingEnabled);
    }

    @Override
    public void disposeUIResources() {
        panel = null;
    }
}
