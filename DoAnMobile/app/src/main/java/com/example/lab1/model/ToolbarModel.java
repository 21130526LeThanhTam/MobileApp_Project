package com.example.lab1.model;

import java.util.List;

public class ToolbarModel {
    boolean success;
    String message;
    List<Toolbar> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Toolbar> getResult() {
        return result;
    }

    public void setResult(List<Toolbar> result) {
        this.result = result;
    }
}
