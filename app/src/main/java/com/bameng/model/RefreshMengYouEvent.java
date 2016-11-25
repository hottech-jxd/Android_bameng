package com.bameng.model;

/**
 * Created by Administrator on 2016/11/21.
 */

public class RefreshMengYouEvent {

    private int type=0;

    public RefreshMengYouEvent(int type){
        this.type=type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
