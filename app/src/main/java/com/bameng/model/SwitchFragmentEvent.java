package com.bameng.model;

/**
 * Created by Administrator on 2016/11/14.
 */

public class SwitchFragmentEvent {
    private String fragnmentName;

    public String getFragnmentName() {
        return fragnmentName;
    }

    public void setFragnmentName(String fragnmentName) {
        this.fragnmentName = fragnmentName;
    }

    public SwitchFragmentEvent(String fragnmentName){
        this.fragnmentName= fragnmentName;
    }
}
