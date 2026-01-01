package com.gilleez.openaiassistant.actions;

import com.gilleez.openaiassistant.core.PromptTemplates;
import com.gilleez.openaiassistant.settings.GilleezSettingsState;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class RefactorSelectionAction extends AnAction {

    private final static ExecuteCommand executeCommand = new ExecuteCommand();
    private final GilleezSettingsState state = ServiceManager.getService(GilleezSettingsState.class);

    @Override
    public void update(AnActionEvent e) {
        var editor = e.getData(CommonDataKeys.EDITOR);
        boolean hasSelection = editor != null && editor.getSelectionModel().hasSelection();
        e.getPresentation().setEnabledAndVisible(hasSelection);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        var editor = e.getData(CommonDataKeys.EDITOR);
        Project project = e.getProject();
        if (editor == null || project == null) return;

        String selected = editor.getSelectionModel().getSelectedText();
        if (selected == null || selected.isBlank()) return;

        var vfile = editor.getVirtualFile();
        if (vfile == null) return;

        if (state.getApiKey().isBlank()) {
            Messages.showErrorDialog(project, "API key is empty. Configure it first.", "Gilleez OpenAI Assistant");
            return;
        }

        String languageHint = vfile.getExtension() != null ? vfile.getExtension() : "text";
        String prompt = PromptTemplates.refactorPrompt(selected, vfile.getPath(), languageHint);

        new Thread(executeCommand.sendCommandAndExecute(prompt, project, "Refactor"), "gilleez-refactor").start();
    }
}
