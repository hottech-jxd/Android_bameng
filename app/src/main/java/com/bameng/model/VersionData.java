package com.bameng.model;

/**
 * Created by 47483 on 2016.11.07.
 */

public class VersionData {

    private String serverVersion;
    private String updateTip;
    private String updateUrl;
    /***
     * 0:无更新，1：整包更新，2：强制更新
     */
    private int updateType;

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public String getUpdateTip() {
        return updateTip;
    }

    public void setUpdateTip(String updateTip) {
        this.updateTip = updateTip;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public int getUpdateType() {
        return updateType;
    }

    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }


}
