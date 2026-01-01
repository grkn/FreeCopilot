package com.gilleez.openaiassistant.core;

public final class PromptTemplates {

    // gilleez: Global instruction. Keep short to save tokens.
    public static final String BASE_INSTRUCTIONS =
            "You are a senior software engineer. Be precise. If asked for a patch, output ONLY unified diff. ";

    private PromptTemplates() {
    }

    public static String explainPrompt(String selectedCode, String languageHint) {
        return BASE_INSTRUCTIONS +
                "Task: Explain the selected code in clear steps. " +
                "Language: " + languageHint + " " +
                "Selected code: " +
                "```" + languageHint + " " +
                selectedCode + " " +
                "``` ";
    }

    public static String refactorPrompt(String selectedCode, String filePath, String languageHint) {
        return BASE_INSTRUCTIONS +
                "Task: Refactor the selected code for readability and maintainability. " +
                "Requirements: " +
                "- Preserve behavior. " +
                "- Output ONLY unified diff for file: " + filePath + " " +
                "Selected code: " +
                "```" + languageHint + " " +
                selectedCode + " " +
                "``` ";
    }
}
