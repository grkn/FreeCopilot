package com.gilleez.openaiassistant.core;

public final class OutputExtractor {

    private OutputExtractor() {
    }

    public static boolean looksLikeUnifiedDiff(String text) {
        return text.contains("--- ") && text.contains("+++ ") && text.contains("@@");
    }
}
