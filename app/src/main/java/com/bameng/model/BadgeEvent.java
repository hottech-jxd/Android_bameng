package com.bameng.model;

import android.hardware.camera2.params.BlackLevelPattern;

/**
 * Created by Administrator on 2016/12/16.
 */

public class BadgeEvent {

    private boolean showNew=false;
    private boolean showBusiness = false;

    public BadgeEvent(boolean showNew, boolean showBusiness){
        this.showNew = showNew;
        this.showBusiness=showBusiness;
    }

    public boolean isShowNew() {
        return showNew;
    }

    public void setShowNew(boolean showNew) {
        this.showNew = showNew;
    }

    public boolean isShowBusiness() {
        return showBusiness;
    }

    public void setShowBusiness(boolean showBusiness) {
        this.showBusiness = showBusiness;
    }
}
