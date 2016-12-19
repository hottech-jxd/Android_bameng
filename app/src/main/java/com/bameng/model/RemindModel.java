package com.bameng.model;

/**
 * Created by Administrator on 2016/12/15.
 */

public class RemindModel {
    private int messageCount;
    private boolean businessRemind;

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public boolean isBusinessRemind() {
        return businessRemind;
    }

    public void setBusinessRemind(boolean businessRemind) {
        this.businessRemind = businessRemind;
    }
}
