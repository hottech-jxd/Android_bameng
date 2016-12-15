package com.bameng.model;


/**
 * Created by Administrator on 2016/11/23.
 */

public class RefreshUserDataEvent {
    private UserData userData;

    public RefreshUserDataEvent(UserData userData){
        this.userData = userData;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}
