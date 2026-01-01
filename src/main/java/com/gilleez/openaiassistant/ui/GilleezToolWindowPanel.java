package com.gilleez.openaiassistant.ui;

import com.gilleez.openaiassistant.api.ChatGptCodexClient;
import com.gilleez.openaiassistant.api.ChatGptInstances;
import com.gilleez.openaiassistant.core.Content;
import com.gilleez.openaiassistant.core.Response;
import com.gilleez.openaiassistant.settings.GilleezSettingsState;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import org.apache.commons.collections.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Simple ToolWindow panel with:
 * - input field
 * - output area (streaming supported)
 * <p>
 * gilleez: Replace with a richer chat UI if you want conversation history, etc.
 */
public class GilleezToolWindowPanel extends JPanel {

    private final Project project;
    private final GilleezSettingsState state = ServiceManager.getService(GilleezSettingsState.class);
    private final JBTextArea output = new JBTextArea();
    private final JTextField input = new JTextField();
    private final JButton runButton = new JButton("Run");
    private final JButton cancelButton = new JButton("Cancel");
    private final AtomicBoolean cancelled = new AtomicBoolean(false);

    public GilleezToolWindowPanel(Project project) {
        super(new BorderLayout());
        this.project = project;

        output.setEditable(false);

        JPanel top = new JPanel(new BorderLayout());
        top.add(input, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.add(runButton);
        buttons.add(cancelButton);
        top.add(buttons, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
        add(new JBScrollPane(output), BorderLayout.CENTER);

        cancelButton.setEnabled(false);

        runButton.addActionListener(e -> runPrompt());
        cancelButton.addActionListener(e -> {
            // gilleez: For true cancellation, also abort OkHttp Call (call.cancel()).
            cancelled.set(true);
            cancelButton.setEnabled(false);
            runButton.setEnabled(true);
        });
    }

    private void runPrompt() {
        if (state.getApiKey().isBlank()) {
            Messages.showErrorDialog("API key is empty. Configure it first.", "Gilleez OpenAI Assistant");
            return;
        }

        output.setText("");
        cancelled.set(false);
        cancelButton.setEnabled(true);
        runButton.setEnabled(false);

        String prompt = input.getText().trim();

        new Thread(() -> {
            try {
                ChatGptCodexClient client = ChatGptInstances.CHAT_GPT_INSTANCES.getInstance(ChatGptCodexClient.class);
                if (state.streamingEnabled) {
                    // TODO
                } else {
                    Response resp = client.fetchResultFromCodex(state.model, prompt);
                    String text = resp.getOutput().stream()
                            .filter(output -> CollectionUtils.isNotEmpty(output.getContent()))
                            .flatMap(output -> output.getContent().stream())
                            .map(Content::getText)
                            .collect(Collectors.joining(System.lineSeparator()));
                    SwingUtilities.invokeLater(() -> output.setText(text));
                }
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        Messages.showErrorDialog(project,
                                ex.getMessage() != null ? ex.getMessage() : "Unknown error",
                                "Gilleez OpenAI Assistant"));
            } finally {
                SwingUtilities.invokeLater(() -> {
                    cancelButton.setEnabled(false);
                    runButton.setEnabled(true);
                });
            }
        }, "gilleez-openai-run").start();
    }
}
