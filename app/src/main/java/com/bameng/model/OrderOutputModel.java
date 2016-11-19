package com.bameng.model;

import java.util.List;

/**
 * Created by Administrator on 2016/11/18.
 */
public class OrderOutputModel extends BaseModel {
    private List<OrderModel> data;

    public List<OrderModel> getData() {
        return data;
    }

    public void setData(List<OrderModel> data) {
        this.data = data;
    }
}
