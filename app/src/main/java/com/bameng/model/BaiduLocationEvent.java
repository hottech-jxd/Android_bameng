package com.bameng.model;

/**
 * Created by Administrator on 2016/11/11.
 */

public class BaiduLocationEvent {
    BaiduLocation model;
    public BaiduLocationEvent(BaiduLocation model){
        this.model=model;
    }

    public BaiduLocation getModel() {
        return model;
    }

    public void setModel(BaiduLocation model) {
        this.model = model;
    }
}
