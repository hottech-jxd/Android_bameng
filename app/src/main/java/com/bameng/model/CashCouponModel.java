package com.bameng.model;

import java.math.BigDecimal;

/**
 * 现金劵 实体
 * Created by Administrator on 2016/11/24.
 */
public class CashCouponModel {
    private int ID;
    private BigDecimal money;
    private String name;
    private String due;
    private String url;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
