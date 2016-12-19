package com.bameng.model;

/**
 * Created by Administrator on 2016/12/16.
 */

public class BadgeBusinessEvent {
    private boolean showBusiness = false;
    public BadgeBusinessEvent( boolean showBusiness){
        this.showBusiness = showBusiness;
    }

    public boolean isShowBusiness() {
        return showBusiness;
    }

    public void setShowBusiness(boolean showBusiness) {
        this.showBusiness = showBusiness;
    }
}
