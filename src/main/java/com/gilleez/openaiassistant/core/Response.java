package com.gilleez.openaiassistant.core;

import java.util.List;

/*
{
  "id": "resp_123",
  "object": "response",
  "created_at": 1740855869,
  "status": "completed",
  "error": null,
  "output": [
    {
      "id": "msg_123",
      "type": "message",
      "role": "assistant",
      "content": [
        { "type": "output_text", "text": "…", "annotations": [] }
      ]
    }
  ],
  "usage": { "input_tokens": 0, "output_tokens": 0, "total_tokens": 0 }
}
 */
public class Response {
    private String id;
    private String object;
    private Long createdAt;
    private String status;
    private String error;
    private List<Output> output;

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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Output> getOutput() {
        return output;
    }

    public void setOutput(List<Output> output) {
        this.output = output;
    }


}
