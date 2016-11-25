package com.bameng.model;

/**
 * Created by Administrator on 2016/11/22.
 */

public class RefreshCashEvent {
    private int type=0;
    public RefreshCashEvent(int type){
        this.type=type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
