package com.gilleez.openaiassistant.actions;

import com.gilleez.openaiassistant.api.ChatGptCodexClient;
import com.gilleez.openaiassistant.api.ChatGptInstances;
import com.gilleez.openaiassistant.core.Content;
import com.gilleez.openaiassistant.core.OutputExtractor;
import com.gilleez.openaiassistant.core.Response;
import com.gilleez.openaiassistant.settings.GilleezSettingsState;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.collections.CollectionUtils;
import org.jspecify.annotations.NonNull;

import javax.swing.*;
import java.util.stream.Collectors;

public class ExecuteCommand {

    private final GilleezSettingsState state = ServiceManager.getService(GilleezSettingsState.class);

    public @NonNull Runnable sendCommandAndExecute(String prompt, Project project, String type) {
        return () -> {
            try {
                ChatGptCodexClient client = ChatGptInstances.CHAT_GPT_INSTANCES.getInstance(ChatGptCodexClient.class);
                Response resp = client.fetchResultFromCodex(state.model, prompt);

                String text = resp.getOutput().stream()
                        .filter(output -> CollectionUtils.isNotEmpty(output.getContent()))
                        .flatMap(output -> output.getContent().stream())
                        .map(Content::getText)
                        .collect(Collectors.joining(System.lineSeparator()));

                if (type.equals("Explain")) {
                    SwingUtilities.invokeLater(() ->
                            Messages.showInfoMessage(project,
                                    text
                                    , "Explain"));
                } else {
                    SwingUtilities.invokeLater(() -> {
                        if (!OutputExtractor.looksLikeUnifiedDiff(text)) {
                            // gilleez: Decide fallback behavior when model doesn't produce a diff.
                            Messages.showInfoMessage(project, text, "Refactor (No diff)");
                            return;
                        }
                        // gilleez: Next step: show diff viewer and apply patch to file.
                        Messages.showInfoMessage(project, text, "Refactor (Diff)");
                    });
                }

            } catch (Exception ex) {
                if (type.equals("Explain")) {
                    SwingUtilities.invokeLater(() ->
                            Messages.showErrorDialog(project,
                                    ex.getMessage() != null ? ex.getMessage() : "Unknown error",
                                    "Explain"));
                } else {
                    SwingUtilities.invokeLater(() ->
                            Messages.showErrorDialog(project,
                                    ex.getMessage() != null ? ex.getMessage() : "Unknown error",
                                    "Refactor"));
                }

            }
        };
    }
}
