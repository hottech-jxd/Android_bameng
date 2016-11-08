package com.bameng.model;

import java.io.Serializable;

/**
 * BaseModel
 */
public class BaseModel implements Serializable {

    private int status;
    private String statusText;

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }



    public int getStatus() {

        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}