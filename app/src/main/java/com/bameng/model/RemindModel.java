package com.bameng.model;

/**
 * Created by Administrator on 2016/12/15.
 */

public class RemindModel {
    /***
     * 我的留言未读数量
     */
    private int messageCount;
    /***
     * 我的业务未读数量
     */
    private boolean businessRemind;
    /***
     * 我的消息 - 发送消息未读数量
     */
    private int messagePushCount;
    /***
     * 我的消息 - 接受消息未读数量
     */
    private int messagePullCount;

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

    public int getMessagePushCount() {
        return messagePushCount;
    }

    public void setMessagePushCount(int messagePushCount) {
        this.messagePushCount = messagePushCount;
    }

    public int getMessagePullCount() {
        return messagePullCount;
    }

    public void setMessagePullCount(int messagePullCount) {
        this.messagePullCount = messagePullCount;
    }
}
