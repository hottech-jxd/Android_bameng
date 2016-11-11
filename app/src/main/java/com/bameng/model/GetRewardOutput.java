package com.bameng.model;

/**
 * Created by 47483 on 2016.11.10.
 */

public class GetRewardOutput extends BaseModel {
    RewardsSettingModel data;

    public RewardsSettingModel getData() {
        return data;
    }

    public void setData(RewardsSettingModel data) {
        this.data = data;
    }
}
