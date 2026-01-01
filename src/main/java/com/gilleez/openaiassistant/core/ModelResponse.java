package com.gilleez.openaiassistant.core;

import java.util.List;

/**
 * {
 * "object": "list",
 * "data": [
 * {
 * "id": "gpt-3.5-turbo",
 * "object": "model",
 * "created": 1677610602,
 * "owned_by": "openai"
 * }
 * ]
 * }
 */
public class ModelResponse {

    private String object;
    private List<Data> data;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {
        private String id;
        private String object;
        private Long created;
        private String ownedBy;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getObject() {
            return object;
        }

        public void setObject(String object) {
            this.object = object;
        }

        public Long getCreated() {
            return created;
        }

        public void setCreated(Long created) {
            this.created = created;
        }

        public String getOwnedBy() {
            return ownedBy;
        }

        public void setOwnedBy(String ownedBy) {
            this.ownedBy = ownedBy;
        }
    }
}
