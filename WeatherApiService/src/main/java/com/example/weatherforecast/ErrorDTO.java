package com.example.weatherforecast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ErrorDTO {
    private Date timestamp;
    private int status;
    private List<String> errors = new ArrayList<>();
    private String path;

    public ErrorDTO() {
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void addError(String error) {
        errors.add(error);
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
