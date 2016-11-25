package com.bameng.model;

/**
 * Created by Administrator on 2016/11/19.
 */

public class MengModel extends  UserData {
    private boolean isDoing=false;

    private int ID;

    private String Mobile;

    /***
     * 0 未审核，1 同意，2 拒绝
     */
    private int Status;

    private String StatusName;

    private String UserName;


    public boolean isDoing() {
        return isDoing;
    }

    public void setDoing(boolean doing) {
        isDoing = doing;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String statusName) {
        StatusName = statusName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
