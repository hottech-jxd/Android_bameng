package com.bameng.model;

/**
 * Created by Administrator on 2016/11/19.
 */

public class OrderDetailOutputModel extends BaseModel {
    private OrderModel data;

    public OrderModel getData() {
        return data;
    }

    public void setData(OrderModel data) {
        this.data = data;
    }
}
