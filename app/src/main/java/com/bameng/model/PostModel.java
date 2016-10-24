package com.bameng.model;

import android.provider.ContactsContract;

import java.util.Map;

/**
 * Created by 47483 on 2016.10.21.
 */

public class PostModel {
    private int status;
    private String statusText;
    private Map data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }
}
