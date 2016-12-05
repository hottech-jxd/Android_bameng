package com.bameng.model;

/**
 * Created by Administrator on 2016/11/30.
 */

public class SetRightVisibleEvent {
    private boolean show = false;
    private String tabName="";

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
    public SetRightVisibleEvent( String tabName , boolean isShow){
        this.tabName = tabName;
        this.show = isShow;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }
}
