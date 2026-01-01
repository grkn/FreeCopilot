package com.gilleez.openaiassistant.core;

import java.util.List;

public class Content {
    private String type;
    private String text;
    private List<Object> annotations;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Object> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Object> annotations) {
        this.annotations = annotations;
    }
}
