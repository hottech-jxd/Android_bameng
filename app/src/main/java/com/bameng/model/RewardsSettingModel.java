package com.bameng.model;

import java.math.BigDecimal;

/**
 * Created by 47483 on 2016.11.10.
 */

public class RewardsSettingModel {
    String CreateTime;
    int CustomerReward;
    int ID;
    int OrderReward;
    int ShopReward;
    String UpdateTime;
    int UserId;
    String ExtraReward;

    ScoreConfig scoreConfig;

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public int getCustomerReward() {
        return CustomerReward;
    }

    public void setCustomerReward(int customerReward) {
        CustomerReward = customerReward;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getOrderReward() {
        return OrderReward;
    }

    public void setOrderReward(int orderReward) {
        OrderReward = orderReward;
    }

    public int getShopReward() {
        return ShopReward;
    }

    public void setShopReward(int shopReward) {
        ShopReward = shopReward;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getExtraReward() {
        return ExtraReward;
    }

    public void setExtraReward(String extraReward) {
        ExtraReward = extraReward;
    }

    public ScoreConfig getScoreConfig() {
        return scoreConfig;
    }

    public void setScoreConfig(ScoreConfig scoreConfig) {
        this.scoreConfig = scoreConfig;
    }
}
