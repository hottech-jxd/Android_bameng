package com.bameng.model;

/**
 * Created by Administrator on 2016/11/30.
 */

public class SetRightVisibleEvent {
    private boolean show = false;

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
    public SetRightVisibleEvent(boolean isShow){
        this.show = isShow;
    }
}
