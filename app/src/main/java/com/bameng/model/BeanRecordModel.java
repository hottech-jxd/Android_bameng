package com.bameng.model;

import java.math.BigDecimal;

import static android.R.attr.id;

/**
 * Created by Administrator on 2016/11/22.
 */

public class BeanRecordModel {
    private int ID;
    private String remark;
    private int status;
    private BigDecimal money;
    private  long time;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
