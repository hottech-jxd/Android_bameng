package com.bameng.model;

import java.util.List;

/**
 * Created by Administrator on 2016/11/24.
 */
public class CashCouponOutputModel extends BaseModel {
    private CashCouponList data;

    public CashCouponList getData() {
        return data;
    }

    public void setData(CashCouponList data) {
        this.data = data;
    }

    public class CashCouponList{
        List<CashCouponModel> list;

        public List<CashCouponModel> getList() {
            return list;
        }

        public void setList(List<CashCouponModel> list) {
            this.list = list;
        }
    }
}
