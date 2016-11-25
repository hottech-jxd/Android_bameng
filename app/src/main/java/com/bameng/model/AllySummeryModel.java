package com.bameng.model;

/**
 * Created by Administrator on 2016/11/24.
 */

public class AllySummeryModel {
    private int CustomerAmount;
    private int CustomerRank;
    private int AllyAmount;
    private int OrderSuccessAmount;
    private int OrderRank;

    public int getCustomerAmount() {
        return CustomerAmount;
    }

    public void setCustomerAmount(int customerAmount) {
        CustomerAmount = customerAmount;
    }

    public int getCustomerRank() {
        return CustomerRank;
    }

    public void setCustomerRank(int customerRank) {
        CustomerRank = customerRank;
    }

    public int getAllyAmount() {
        return AllyAmount;
    }

    public void setAllyAmount(int allyAmount) {
        AllyAmount = allyAmount;
    }

    public int getOrderSuccessAmount() {
        return OrderSuccessAmount;
    }

    public void setOrderSuccessAmount(int orderSuccessAmount) {
        OrderSuccessAmount = orderSuccessAmount;
    }

    public int getOrderRank() {
        return OrderRank;
    }

    public void setOrderRank(int orderRank) {
        OrderRank = orderRank;
    }
}
