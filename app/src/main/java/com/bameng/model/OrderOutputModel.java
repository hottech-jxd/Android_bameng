package com.bameng.model;

import java.util.List;

import static android.R.attr.data;

/**
 * Created by Administrator on 2016/11/18.
 */
public class OrderOutputModel extends BaseModel {
    private OrderList data;

    public OrderList getData() {
        return data;
    }

    public void setData(OrderList data) {
        this.data = data;
    }

    public class OrderList{
        private List<OrderModel> list;

        public List<OrderModel> getList() {
            return list;
        }

        public void setList(List<OrderModel> list) {
            this.list = list;
        }
    }
}
